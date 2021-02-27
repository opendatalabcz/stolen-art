from core.models import Painting
from django.core.files import File
import requests
import cv2

def save_images_to_db(paintings_paths):

    for path in paintings_paths:
        
        name = str(path).split('/')[-1].split('.')[0]
        image_file = open(path, 'rb')
        image_file = File(image_file)

    
        p = Painting(name=name)
        p.image.save(str(path),image_file,save=True)
        
       


    print("Saving to DB was a success.")

