from core.models import Painting, PaintingDescriptors
from painting.orb import ORB
from painting.data_loader import DataLoader as dl
from django.core.files import File
import cv2

def save_images_to_db(paintings_paths):

    orbh = ORB(500)

    counter = 0

    
    for path in paintings_paths:
        
        if counter % 10 == 0:
            print(f"Processing paintings; paintings processed: {counter}")

        name = str(path).split('/')[-1].split('.')[0]
        image_file = open(path, 'rb')
        image_file = File(image_file)

    
        p = Painting(name=name)
        p.image.save(str(path),image_file,save=True)

        image = dl.file_to_image(image_file)
        im_descriptors = orbh.detect_and_compute(image, return_keypoints=False)

        
        if im_descriptors is None:
            # no descriptors, unrecognizable image
            continue


        im_descriptors = im_descriptors.tolist()

        d = PaintingDescriptors(painting=p,descriptors=im_descriptors)
        d.save()

        counter = counter + 1
        #if counter == 1800:
        #    break
        


    print("Saving to DB was a success.")

