import numpy
import pandas
from keras.models import Sequential
from keras.layers import Dense
from keras.wrappers.scikit_learn import KerasClassifier
from keras.utils import np_utils
from sklearn.cross_validation import cross_val_score
from sklearn.cross_validation import KFold
from sklearn.preprocessing import LabelEncoder
from sklearn.pipeline import Pipeline

numpy.random.seed(0)

dataframe   = pandas.read_csv("iris.csv", header=None)
dataset     = dataframe.values

X = dataset[:,0:4].astype(float)
Y = dataset[:,4]

# Preprocess the labels

# LabelEncoder from scikit-learn turns each text label
# (e.g "Iris-setosa", "Iris-versicolor") into a vector
# In this case, each of the three labels are just assigned
# a number from 0-2.
encoder = LabelEncoder()
encoder.fit(Y)
encoded_Y = encoder.transform(Y)

# to_categorical converts the numbered labels into a one-hot vector
dummy_y = np_utils.to_categorical(encoded_Y)

def baseline_model():
    model = Sequential()
    model.add(Dense(4, input_dim=4, init='normal', activation='relu'))
    model.add(Dense(3, init='normal', activation='sigmoid'))
    model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])

    return model

estimator = KerasClassifier(build_fn=baseline_model, nb_epoch=200, batch_size=5, verbose=0)

kfold = KFold(n=len(X), n_folds=10, shuffle=True, random_state=0)
results = cross_val_score(estimator, X, dummy_y, cv=kfold)
print("Baseline: %.2f%% (%.2f%%)" % (results.mean()*100, results.std()*100))

#TODO:
#       - persist models as .h5 or other format
#       - write down some information regarding performance and other valuable metadata