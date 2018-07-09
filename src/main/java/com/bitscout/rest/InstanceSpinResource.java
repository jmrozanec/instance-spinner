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
package com.bitscout.rest;

import com.bitscout.services.InstanceSpinService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(value = "/rest/instances")
public class InstanceSpinResource {
    @Resource
    private InstanceSpinService instanceSpinService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    Map<String, String> spinInstance(@RequestParam(value = "author", required = true) String author,
                      @RequestParam(value = "description", required = false, defaultValue = "No description provided") String description,
                      @RequestParam(value = "randomseed", required = false, defaultValue = "1234") int randomseed,
                      @RequestParam(value = "depth", required = false, defaultValue = "2") int depth,
                      @RequestParam(value = "epochs", required = false, defaultValue = "50") int epochs,
                      @RequestParam(value = "batchsize", required = false, defaultValue = "20") int batchsize,
                      HttpServletResponse response) throws Exception {
        return instanceSpinService.spinInstance(author, description, randomseed, depth, epochs, batchsize);
    }
}
