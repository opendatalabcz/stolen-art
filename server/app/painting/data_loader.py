import pathlib
import cv2


class DataLoader:

   

    def process_paintings(directory_name):
    # load from ../datasets/directory_name
        
        data_dir = pathlib.Path(directory_name)
        orb = cv2.ORB_create(nfeatures=500)
        print("Created ORB object.")

