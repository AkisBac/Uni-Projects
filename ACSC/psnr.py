import numpy as np
import math

import configs as cfgs

if cfgs.NORMALIZE:
	PIXEL_MAX = 1.0
else:
	PIXEL_MAX = 255.0


def psnr(ref, img):
    assert ref.shape == img.shape, 'Image shapes mis-match'
    mse = np.mean( (ref - img) ** 2 )
    if mse == 0:
        return 100
    else:
        return 20 * math.log10(PIXEL_MAX / math.sqrt(mse))
