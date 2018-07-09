/*
 * Copyright 2017 jmrozanec
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitscout.services;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class InstanceSpinService {
    private static final String SUBNET_ID = "subnet-id";
    private static final String [] SECURITY_GROUPS = new String[]{"sg-01", "sg-02"};
    private static final String SSH_KEY_NAME = "NameOfsshKey";
    private AmazonEC2Client client;

    public InstanceSpinService(String accessKey, String secretKey){
        this.client = new AmazonEC2Client(new BasicAWSCredentials(accessKey, secretKey));
    }

    public Map<String, String> spinInstance(String author, String description, int randomseed, int depth, int epochs, int batchsize){
        String userData = "";
        /* other possible attributes:
            - dataset hash: we can compare models regarding same data
            - test hash: we can compare models regarding same test data (perhaps we want to test different amounts of data if information may be time sensitive)
         */

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            userData = IOUtils.toString(classLoader.getResourceAsStream("user-data/init-script.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        userData =
                userData
                        .replace("{EXPERIMENT_AUTHOR}", author)
                        .replace("{EXPERIMENT_DESCRIPTION}", description)
                        .replace("{RANDOM_SEED}", ""+randomseed)
                        .replace("{EXPERIMENT_DLA_DEPTH}", ""+depth)
                        .replace("{EXPERIMENT_EPOCHS}", ""+epochs)
                        .replace("{EXPERIMENT_BATCHSIZE}", ""+batchsize);

        InstanceNetworkInterfaceSpecification network =
                new InstanceNetworkInterfaceSpecification()
                        .withDeviceIndex(0)
                        .withAssociatePublicIpAddress(true)
                        .withSubnetId(SUBNET_ID)
                        .withGroups(SECURITY_GROUPS);
        TagSpecification tags =
                new TagSpecification()
                        .withResourceType("instance")
                        .withTags(new Tag("label", "pipeline"));
        BlockDeviceMapping blockDeviceMapping =
                new BlockDeviceMapping()
                        .withDeviceName("/dev/xvda")
                        .withEbs(new EbsBlockDevice().withVolumeSize(500));

        RunInstancesRequest request =
                new RunInstancesRequest("ami-a3b3d4b5", 1, 1)
                        .withInstanceType("p2.8xlarge")
                        .withKeyName(SSH_KEY_NAME)
                        .withNetworkInterfaces(new InstanceNetworkInterfaceSpecification[]{network})
                        .withUserData(Base64.getEncoder().encodeToString(userData.getBytes()))
                .withTagSpecifications(tags)
                .withBlockDeviceMappings(blockDeviceMapping);

        RunInstancesResult reservation = client.runInstances(request);
        Instance instance = reservation.getReservation().getInstances().get(0);

        Map<String, String> instanceData = Maps.newHashMap();
        instanceData.put("instance_id", instance.getInstanceId());
        instanceData.put("instance_ip", instance.getPrivateIpAddress());
        return instanceData;
    }
}
