augmenters_list = [
    
      
    iaa.GaussianBlur(sigma=(0, 3.0)), # blur images with a sigma of 0 to 3.0
   
    iaa.Sometimes(0.25,
                iaa.JpegCompression(compression=(0, 30)) ),# degrade the quality of images by JPEG-compressing them.  
        
    iaa.Affine(
        scale=(0.9,1.1),
        translate_percent={"x": (-0.05, 0.05), "y": (-0.05, 0.05)}, # shifting on axis
        rotate=(-20, 20), # rotating
        ),

    iaa.Crop(px=(0, 16)), # crop images from each side by 0 to 16px (randomly chosen)
    iaa.LinearContrast((0.75, 1.5)),
        
    iaa.Sometimes(0.25,
        iaa.Cutout(fill_mode="gaussian", fill_per_channel=True) ),
        
        
    ]
