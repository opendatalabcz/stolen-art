import cv2
import numpy as np

def get_best_matches(found_painting_descriptor, all_descriptors, n_nearest=1):

    matches = get_nearest_matches_flann(found_painting_descriptor, all_descriptors, n_nearest=n_nearest)


    return matches

def get_nearest_matches_flann(found_painting_descriptor, all_descriptors, n_nearest=1, k = 2, min_matches = 10):

    # 1. parametr FlannBasedMatcheru
    FLANN_INDEX_LSH = 6
    index_params= dict(algorithm = FLANN_INDEX_LSH,
                    table_number = 6, # 12
                    key_size = 12,     # 20
                    multi_probe_level = 1) #2

    # 2. parametr FlannBasedMatcheru
    search_params = dict(checks=50)   # or pass empty dictionary

    matches_count = {}
    matches_count[-1] = 0
    
    matches = {}
    matches[-1] = None
    
    matches_masks = {}
    matches_masks[-1] = None
    
    
    for p_index, curr_stolen_descriptors in all_descriptors.items():
        
        curr_stolen_descriptors = np.array(curr_stolen_descriptors)

        # TODO - this was lazy fixed
        if found_painting_descriptor is None or len(found_painting_descriptor) <= k or curr_stolen_descriptors is None or len(curr_stolen_descriptors) <= k:
            best_match_index = -1
            continue
        
        # Cross check parametr
        flann = cv2.FlannBasedMatcher(index_params, search_params)

        # Perform the matching between the ORB descriptors of the training image and the test image

        matches[p_index] = flann.knnMatch(curr_stolen_descriptors, found_painting_descriptor, k)
        # deskriptor = "fingerprint" keypointu, vektor 0 a 1, napr. BRIEF rozmaze misto a z toho spocita vektor
        # ORB muze pouzivat rBRIEF, tzn. pocita i s rotaci obrazu

        ok_matches_num = 0

        # Need to draw only good matches, so create a mask
        matches_masks[p_index] = [[0,0] for i in range(len(matches[p_index]))]
        # ratio test as per Lowe's paper
        for i, candidates in enumerate(matches[p_index]):
            if (len(candidates)<2):
                continue # nedostatek bodu pro porovnani
            m, n = candidates    
            if m.distance < 0.7*n.distance:
                matches_masks[p_index][i]=[1,0]
                ok_matches_num = ok_matches_num + 1 
        
        matches_count[p_index] = ok_matches_num
        
    
        
        # print("Pocet prijatelnych matchu: ", ok_matches_num)

    best_matches_indices = dict(sorted(matches_count.items(), key = lambda g: (g[1]), reverse = True)[:n_nearest]).keys()
        
    return best_matches_indices