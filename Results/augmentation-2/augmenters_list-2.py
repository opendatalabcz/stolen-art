# specify the augmentors to be used
augmenters_list = [
    
    # Gaussian blur images with a sigma from range
    iaa.GaussianBlur(sigma=(0, 5.0)), 
    
    # apply JpegCompression on some images
    # degrade the quality of images by JPEG-compressing them. 
    iaa.Sometimes(0.50,
                iaa.JpegCompression(compression=(0, 30)) ), 
        
    iaa.Affine(
        scale=(0.3,2), # zooming in/out
        translate_percent={"x": (-0.05, 0.05), "y": (-0.05, 0.05)}, # shifting on axis
        rotate=(-50, 50), # rotating
        ),
    
    # changes contrast
    iaa.LinearContrast((0.75, 1.5)),
        
    # sometimes cut a square hole in an image and fill it with noise
    iaa.Sometimes(0.25,
        iaa.Cutout(fill_mode="gaussian", fill_per_channel=True) ),
        
    ]
