# specify the augmentors to be used
augmenters_list = [
    
      
    iaa.GaussianBlur(sigma=(0, 5.0)), # blur images with a sigma of 0 to 5.0
   
    iaa.Sometimes(0.50,
                iaa.JpegCompression(compression=(0, 30)) ),# degrade the quality of images by JPEG-compressing them.  
        
    iaa.Affine(
        scale={"x": (0.4, 1), "y": (0.4, 1)}, # zooming out
        translate_percent={"x": (-0.05, 0.05), "y": (-0.05, 0.05)}, # shifting on axis
        rotate=(-50, 50), # rotating
        ),

    iaa.LinearContrast((0.75, 1.5)),
        
    iaa.Sometimes(0.25,
        iaa.Cutout(fill_mode="gaussian", fill_per_channel=True) ), # cutout a square, fill it with noise
                                                                   # algo should not rely on just one area of keypoints
        
        
    ]
