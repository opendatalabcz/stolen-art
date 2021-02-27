from painting.data_loader import DataLoader as dl
from painting.data_saver import save_images_to_db
from painting.orb_helper import ORBHelper

def process_dir_to_array(directory):

    paintings_paths = dl.get_paths(directory)
    save_images_to_db(paintings_paths)
    
    #orbh = ORBHelper(500)

    #descriptors = orbh.detect_and_compute_from_paths(paintings_paths)


    
