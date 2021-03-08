from painting.data_loader import DataLoader as dl
from painting.data_saver import save_images_to_db
from painting.orb_helper import ORBHelper
from painting.matchers import get_best_matches

def process_dir_to_array(directory):

    paintings_paths = dl.get_paths(directory)
    save_images_to_db(paintings_paths)
    
    #orbh = ORBHelper(500)

    #descriptors = orbh.detect_and_compute_from_paths(paintings_paths)


def find_similar(uploaded_image, n_nearest=1):

    image = dl.file_to_image(uploaded_image)
    image_descriptors = ORBHelper().detect_and_compute(image, return_keypoints=False)
    
    descriptors_from_db = dl.load_descriptors_from_db()
    
    orbh = ORBHelper()
    best_matches = get_best_matches(image_descriptors, descriptors_from_db, n_nearest=n_nearest)


    #simple_match_test(image_descriptors, descriptors_from_db[2])

    return best_matches
