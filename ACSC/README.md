My thesis for my BSc in Computer Science Engineer on image super resolution.

You can execute the network via mainCNNAE.py. You can set inside the main either TRAIN = 1 for training the model or TRAIN = 0 for testing it.

For training it requires a .h5py image dataset (not included). I used a Multispectral Image dataset for my thesis downgrading each picture by 3 different scales x2,x4,x8 in order to receive the best results.

The model consists of 2 seperate branches receiving 1 image of the same scene, differentiated in spectrum field, in each branch. The two branches are linearly combined in order to receive the high resolution image of the scene.

The network is being trained in order to minimize the MSE.


