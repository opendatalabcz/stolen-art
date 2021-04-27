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

    # get the paths of the paintings that are going to be tested
    paintings_paths = dl.get_paths(directory)

    # load the descriptor of the paintings that are currently in the DB
    descriptors_from_db = dl.load_descriptors_from_db()

    # set metrics counters to 0
    correct_found = 0
    incorrect_found = 0
    not_found = 0

    counter = 0


    for path in paintings_paths:
        image = ORBHelper().load_and_preprocess(path)
        # remove _aug prefix and .jpg postfix
        painting_name = path.split('/')[-1][4:].split('.')[0] 

        descriptors = ORBHelper().detect_and_compute(image,return_keypoints=False)

        best_match_id = get_best_matches(descriptors, descriptors_from_db)
        best_match = Painting.objects.filter(pk__in=best_match_id).values()
        
        found_painting_name = ""

        if len(best_match) > 0:
            found_painting_name = best_match[0]['name']
            print(f"Best match for {painting_name} is {found_painting_name}")
            if found_painting_name == painting_name:
                correct_found += 1
            else:
                incorrect_found += 1
        
        else:
            not_found += 1

        
        counter+=1
        if counter % 25:
            all = (correct_found + incorrect_found + not_found)
            accuracy = correct_found / all
            print(f"Still testing... Tested: {counter}")
            print(f"Paintings found correctly: {correct_found} -- {accuracy*100} %")
            print(f"Paintings founc incorrectly: {incorrect_found} -- {(incorrect_found / all) * 100} %")
            print(f"Paintings not found: {not_found} -- {(not_found / all) * 100} %")


        if counter == 250:
            break

    all = (correct_found + incorrect_found + not_found)

    accuracy = correct_found / all


    print(f"Paintings found correctly: {correct_found} -- {accuracy*100} %")
    print(f"Paintings founc incorrectly: {incorrect_found} -- {(incorrect_found / all) * 100} %")
    print(f"Paintings not found: {not_found} -- {(not_found / all) * 100} %")
        



def find_similar(uploaded_image, n_nearest=1):

    image = dl.file_to_image(uploaded_image)
    image_descriptors = ORBHelper().detect_and_compute(image, return_keypoints=False)
    
    descriptors_from_db = dl.load_descriptors_from_db()
    
    orbh = ORBHelper()
    best_matches = get_best_matches(image_descriptors, descriptors_from_db, n_nearest=n_nearest)


    return best_matches
