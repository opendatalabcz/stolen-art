from painting.data_loader import DataLoader as dl
from painting.data_saver import save_images_to_db
from painting.orb_helper import ORBHelper
from painting.matchers import get_best_matches
from core.models import Painting

import cv2
def process_dir_to_array(directory):

    paintings_paths = dl.get_paths(directory)
    save_images_to_db(paintings_paths)
    
def test_accuracy_from_dir(directory):

    paintings_paths = dl.get_paths(directory)
    descriptors_from_db = dl.load_descriptors_from_db()
    print(len(descriptors_from_db))

    counter = 0
    for path in paintings_paths:
        image = ORBHelper().load_and_preprocess(path)
        painting_name = path.split('/')[-1][4:] 

        descriptors = ORBHelper().detect_and_compute(image,return_keypoints=False)

        best_match_id = get_best_matches(descriptors, descriptors_from_db)
        print(f"Best match ID: {best_match_id}")
        best_match = Painting.objects.filter(pk__in=best_match_id).values()
        
        found_painting_name = ""
        found_ok = 0
        if len(best_match) > 0:
            found_painting_name = best_match[0]['name']
            print(f"Best match for {painting_name} is {found_painting_name}")
            if found_painting_name == painting_name:
                found_ok += 1


        
        counter+=1
        if counter % 100:
            print(f"Still testing... Tested: {counter}")

    print(f"Correctly found {found_ok} from {len(paintings_paths)}")
        



def find_similar(uploaded_image, n_nearest=1):

    image = dl.file_to_image(uploaded_image)
    image_descriptors = ORBHelper().detect_and_compute(image, return_keypoints=False)
    
    descriptors_from_db = dl.load_descriptors_from_db()
    
    orbh = ORBHelper()
    best_matches = get_best_matches(image_descriptors, descriptors_from_db, n_nearest=n_nearest)


    #simple_match_test(image_descriptors, descriptors_from_db[2])

    return best_matches
