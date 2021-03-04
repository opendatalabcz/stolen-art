import pathlib
import cv2
import numpy as np
from PIL import Image



class DataLoader:

    datasets_to_load_directory = pathlib.Path("/app/datasets/")
    
    def get_paths(directory_name):

        data_dir = DataLoader.datasets_to_load_directory / directory_name
        image_count = len(list(data_dir.glob('*')))
        print(f"Number of files in {data_dir}: {image_count}")
        
        paintings_paths = list(data_dir.glob('*'))
        paintings_paths = [str(path) for path in paintings_paths]

        return paintings_paths


    def file_to_image(file):

        pil_img = Image.open(file)
        arr_img = np.array(pil_img)
        cv_image = cv2.cvtColor(arr_img, cv2.COLOR_RGB2BGR)

        return cv_image

    




