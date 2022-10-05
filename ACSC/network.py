#import tensorflow as tf
import tensorflow.compat.v1 as tf
tf.disable_v2_behavior()

import numpy as np
from sklearn import preprocessing

88888
import configs as cfgs


NUM_CHANNELS        = cfgs.NUM_CHANNELS
NUM_FILTERS         = cfgs.NUM_FILTERS
input_kernel_size   = cfgs.input_kernel_size
output_kernel_size  = cfgs.output_kernel_size

DICT_DIM    = cfgs.DICT_DIM

C = L = 5

def build_cnnlista(inputs_):
    print('BUILD NETWORK')

    # 1. PATCH EXTRACTION CONVOLUTIONAL LAYER
    W1  = tf.random_normal([input_kernel_size, input_kernel_size, NUM_CHANNELS, NUM_FILTERS], stddev = .1, dtype = tf.float32)
    W1_ = tf.Variable(W1, name = 'W1')
    h   = tf.nn.conv2d(inputs_, W1_, strides = [1,1,1,1], padding = 'SAME')

    # RESHAPE CNN FEATURES
    size_h = h.shape
    print("size of convolutional features h:", size_h)

    hh = tf.reshape(h, [-1, size_h[1]*size_h[2], NUM_FILTERS])
    size_hh = hh.shape
    print("size of convolutional features hh:", size_hh)

    hhh = tf.reshape(hh, [-1, NUM_FILTERS])
    size_hhh = hhh.shape
    print("size of convolutional features hhh:", size_hhh)

    # 2. LISTA LAYERS
    lista_features, weightlist = build_lista(hhh, num_layers = 2, initial_theta = 0.1) 
    print("size of LISTA FEATURES:", lista_features.shape)

    # 3. PATCH RECONSTRUCTION LAYER: RECONSTRUCT FROM LISTA FEATURES
    # Dx = (1/(C*L))*tf.random_normal([DICT_DIM, NUM_FILTERS], stddev = .1, dtype = tf.float32)
    Dx      = tf.random_normal([DICT_DIM, NUM_FILTERS], stddev = .1, dtype = tf.float32)
    Dx_     = tf.Variable(Dx, name = 'Dx')
    hpatch  = tf.matmul(lista_features, Dx_)

    # RESHAPE LISTA FEATURES
    hpatch = tf.reshape(hpatch, [-1, size_h[1], size_h[2], NUM_FILTERS])
    size_hp = hpatch.shape
    print("size of HR patch FEATURES:", size_hp)

    # 4. PATCH SYNTHESIS CONVOLUTIONAL LAYER 
    W2  = tf.random_uniform([input_kernel_size, input_kernel_size, NUM_FILTERS, NUM_CHANNELS], dtype = tf.float32)
    W2_ = tf.Variable(W2, name = 'W2')
    reconstuction = tf.nn.conv2d(hpatch, W2_, strides = [1,1,1,1], padding = 'SAME')

    print("network output:", reconstuction)

    # gather weights/features for regularization/visualization
    features = {'input_lista_h': h, 'input_lista_hh': hh,'input_lista_hhh': hhh, 'output_lista':lista_features}
    weightlist.append(Dx_)
    weightlist.append(W2_)

    return reconstuction, features, weightlist


def build_ACSC(y_,x_):
    B = 0.1*tf.random.normal([7,7,85,1], stddev = .1, dtype = tf.float32)
    D = B
    S = 0.1*tf.random.normal([7,7,1,85], stddev = .01, dtype = tf.float32)

    B2 = 0.1*tf.random.normal([7,7,42,1], stddev = .1, dtype = tf.float32)
    D2 = B2
    S2 = 0.1*tf.random.normal([7,7,1,42], stddev = .01, dtype = tf.float32)

    weights = {
           'we1'    : tf.Variable(S,    name = 'we1'),
           'wd1'    : tf.Variable(B,    name = 'wd1_X3'),
           'tetta1' : tf.Variable(0.1,  name = 'tetta1'),
           'D'      : tf.Variable(B,    name = 'D'),
           'we2'    : tf.Variable(S,    name = 'we2'),
           'wd2'    : tf.Variable(B,    name = 'wd2'),
           'tetta2' : tf.Variable(0.1,  name = 'tetta2'),
           'we3'    : tf.Variable(S,    name = 'we3'),
           'wd3'    : tf.Variable(B,    name = 'wd3'),
           'tetta3' : tf.Variable(0.1,  name = 'tetta3'),
           'we4'    : tf.Variable(S,    name = 'we4'),
           'wd4'    : tf.Variable(B,    name = 'wd4'),
           'tetta4' : tf.Variable(0.1,  name = 'tetta4'),
           'we5'    : tf.Variable(S,    name = 'we5'),
           'tetta5' : tf.Variable(0.1,  name = 'tetta5')}

    weights2 = {
           'we11'    : tf.Variable(S2,    name = 'we11'),
           'wd11'    : tf.Variable(B2,    name = 'wd11_X3'),
           'tetta11' : tf.Variable(0.1,  name = 'tetta11'),
           'D2'      : tf.Variable(B2,    name = 'D2'),
           'we22'    : tf.Variable(S2,    name = 'we22'),
           'wd22'    : tf.Variable(B2,    name = 'wd22'),
           'tetta22' : tf.Variable(0.1,  name = 'tetta22'),
           'we33'    : tf.Variable(S2,    name = 'we33'),
           'wd33'    : tf.Variable(B2,    name = 'wd33'),
           'tetta33' : tf.Variable(0.1,  name = 'tetta33'),
           'we44'    : tf.Variable(S2,    name = 'we44'),
           'wd44'    : tf.Variable(B2,    name = 'wd44'),
           'tetta44' : tf.Variable(0.1,  name = 'tetta44'),
           'we55'    : tf.Variable(S2,    name = 'we55'),
           'tetta55' : tf.Variable(0.1,  name = 'tetta55')}

    conv1   = tf.nn.conv2d(y_,      weights['we1'], strides = [1,1,1,1], padding = 'SAME')
    zk0     = ShLU(conv1, weights['tetta1'])
    conv2   = tf.nn.conv2d(zk0,     weights['wd1'], strides = [1,1,1,1], padding = 'SAME')
    conv3   = tf.nn.conv2d(conv2,   weights['we2'], strides = [1,1,1,1], padding  = 'SAME')
    zk1     = ShLU(zk0 - conv3 + conv1,weights['tetta2'])
    conv4   = tf.nn.conv2d(zk1,     weights['wd2'], strides = [1,1,1,1], padding = 'SAME')
    conv5   = tf.nn.conv2d(conv4,   weights['we3'], strides = [1,1,1,1], padding  = 'SAME')
    zk2       = ShLU(zk1 - conv5 + conv1,weights['tetta3'])
    conv6   = tf.nn.conv2d(zk2,     weights['wd3'], strides = [1,1,1,1], padding = 'SAME')
    conv7   = tf.nn.conv2d(conv6,   weights['we4'], strides = [1,1,1,1], padding  = 'SAME')
    Z       = ShLU(zk2 - conv7 + conv1,weights['tetta4'])
    #conv8   = tf.nn.conv2d(zk3,     weights['wd4'], strides = [1,1,1,1], padding = 'SAME')
    #conv9   = tf.nn.conv2d(conv8,   weights['we5'], strides = [1,1,1,1], padding  = 'SAME')
    #Z       = ShLU(zk3 - conv9 + conv1,weights['tetta5'])

    conv11   = tf.nn.conv2d(x_,      weights2['we11'], strides = [1,1,1,1], padding = 'SAME')
    zk00     = ShLU(conv11, weights2['tetta11'])
    conv22   = tf.nn.conv2d(zk00,     weights2['wd11'], strides = [1,1,1,1], padding = 'SAME')
    conv33   = tf.nn.conv2d(conv22,   weights2['we22'], strides = [1,1,1,1], padding  = 'SAME')
    zk11     = ShLU(zk00 - conv33 + conv11,weights2['tetta22'])
    conv44   = tf.nn.conv2d(zk11,     weights2['wd22'], strides = [1,1,1,1], padding = 'SAME')
    conv55   = tf.nn.conv2d(conv44,   weights2['we33'], strides = [1,1,1,1], padding  = 'SAME')
    zk22       = ShLU(zk11 - conv55 + conv11,weights2['tetta33'])
    conv66   = tf.nn.conv2d(zk22,     weights2['wd33'], strides = [1,1,1,1], padding = 'SAME')
    conv77   = tf.nn.conv2d(conv66,   weights2['we44'], strides = [1,1,1,1], padding  = 'SAME')
    ZZ       = ShLU(zk22 - conv77 + conv11,weights2['tetta44'])
    #conv88   = tf.nn.conv2d(zk33,     weights2['wd44'], strides = [1,1,1,1], padding = 'SAME')
    #conv99   = tf.nn.conv2d(conv88,   weights2['we55'], strides = [1,1,1,1], padding  = 'SAME')
    #ZZ       = ShLU(zk33 - conv99 + conv11,weights2['tetta55'])

    reconstruction = tf.nn.relu(tf.nn.conv2d(Z, weights['D'], strides = [1,1,1,1], padding = 'SAME'))
    reconstruction2 = tf.nn.relu(tf.nn.conv2d(ZZ, weights2['D2'], strides = [1,1,1,1], padding = 'SAME'))
    reconstructed =  reconstruction + reconstruction2

    return reconstructed, Z, weights, ZZ, weights2


def build_lista(y_, num_layers = 2, initial_theta = 0.1):
    print('BUILD NETWORK LISTA')

    I 	= np.identity(DICT_DIM)
    Dy 	= np.random.normal(0, 1, (DICT_DIM, NUM_FILTERS))
    Dy  = preprocessing.normalize(Dy,axis = 1)
    DyT = Dy.T
    W 	= C*DyT
    S   = I-np.dot(Dy, DyT)

    T = initial_theta*tf.ones(shape=[DICT_DIM])

    # layer 0
    W_         = tf.Variable(W, dtype = tf.float32, name = 'W')
    theta_     = tf.Variable(T, dtype = tf.float32, name='theta')
    threshold_ = tf.constant(np.ones(shape=[DICT_DIM]), dtype = tf.float32, name = 'threshold')
    Wy_        = tf.divide(tf.matmul(y_, W_), theta_)
    zhat_      = tf.multiply(eta(Wy_, threshold_), theta_) 
    # layer 1 ... (num_layers-1)
    for t in range(1, num_layers):
        S_      = tf.Variable(S, dtype = tf.float32, name='S_{0}'.format(t))
        Sz_     = tf.divide(tf.matmul(zhat_, S_), theta_)
        zhat_   = tf.multiply(eta(Sz_ + Wy_, threshold_), theta_) 

    weightlist = [W_, S_]
    return zhat_, weightlist



def ShLU(a, th):
    return tf.sign(a)*tf.maximum(0.0, tf.abs(a)-th)

def eta(r_, lam_):
    "implement a soft threshold function y=sign(r)*max(0,abs(r)-lam)"
    lam_ = tf.maximum(lam_, 0)
    return tf.sign(r_) * tf.maximum(tf.abs(r_) - lam_, 0)


def Convolution2D(input_tensor,
             kernel_shape,
             strides=(1, 1, 1, 1),
             padding='SAME',
             activation=None,
             scope=''):

        kernel_shape = kernel_shape
        strides = strides
        padding = padding
        activation = activation
        scope = scope

        # build kernel
        kernel = tf.Variable(tf.truncated_normal(kernel_shape, stddev=0.001), name='kernel')
        # build bias
        kernel_height, kernel_width, num_input_channels, num_output_channels = kernel.get_shape()
        #bias = tf.Variable(tf.zeros( shape=[num_output_channels]), name='bias')
        bias = tf.Variable(0.1*tf.ones(shape=[num_output_channels]), name='bias')
        # convolution
        conv = tf.nn.conv2d(input_tensor, kernel, strides=strides, padding=padding)
        # activation
        if activation == tf.nn.sigmoid:
            print("activation: sigmoid")
            recon = tf.nn.sigmoid(conv + bias)
            if cfgs.NORMALIZE == 0:
                reconstuction = tf.math.multiply(recon, 255)
            else:
                recon = tf.nn.sigmoid(conv + bias)
        else:
            reconstuction = activation(conv + bias)

        return reconstuction



if __name__ == "__main__":
    inputs_ = tf.compat.v1.placeholder(tf.float32, shape=[None, 56, 56, NUM_CHANNELS])
    W1_ = tf.Variable(tf.random.normal([input_kernel_size, input_kernel_size, NUM_CHANNELS, NUM_FILTERS], stddev = .1, dtype = tf.float32), name = 'W1')
    h = tf.nn.conv2d(inputs_, W1_, strides = [1,1,1,1], padding = 'SAME')

    # RESHAPE CNN FEATURES
    size_h = h.shape
    print("size of convolutional features h:", size_h)

    hh = tf.reshape(h, [-1, size_h[1]*size_h[2], NUM_FILTERS])
    size_hh = hh.shape
    print("size of convolutional features hh:", size_hh)

    hhh = tf.reshape(hh, [-1, NUM_FILTERS])
    size_hhh = hhh.shape
    print("size of convolutional features hhh:", size_hhh)

    
    build_lista(hhh)
