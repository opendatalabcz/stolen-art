import pathlib
import cv2
import numpy as np
from PIL import Image
from core.models import PaintingDescriptors



class DataLoader:

    datasets_to_load_directory = pathlib.Path("/app/datasets/")
    
    def get_paths(directory_name):

        data_dir = DataLoader.datasets_to_load_directory / directory_name
        image_count = len(list(data_dir.glob('*')))
        print(f"Number of files in {data_dir}: {image_count}")
        
        paintings_paths = list(data_dir.glob('*'))
        paintings_paths = [str(path) for path in paintings_paths]

        paintings_paths = sorted(paintings_paths)

        return paintings_paths


    def file_to_image(file):

        pil_img = Image.open(file)
        arr_img = np.array(pil_img)
        cv_image = cv2.cvtColor(arr_img, cv2.COLOR_RGB2BGR)

        cv2.imwrite("last_received_image.jpg", cv_image)

        return cv_image

    def load_descriptors_from_db():

        descs_db = PaintingDescriptors.objects.all().values()
        #print(descs_db[2])
        descs = {}
        for desc_inst in descs_db:
            painting_id = desc_inst['painting_id']
            decriptors_as_list = desc_inst['descriptors']
            descriptors = np.asarray(decriptors_as_list, np.uint8)
            descs[painting_id] = descriptors

        
        return descs

    




