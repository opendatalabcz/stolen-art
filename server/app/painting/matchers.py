import cv2
import numpy as np

def get_best_matches(found_painting_descriptor, all_descriptors, n_nearest=1):

    matches = get_nearest_matches_flann(found_painting_descriptor, all_descriptors, n_nearest=n_nearest)


    return matches

def get_nearest_matches_flann(found_painting_descriptor, all_descriptors, n_nearest=1, k = 2, min_matches = 10):

    matches_count = {}
    matches_count[-1] = 0
        
    matcher = Flann()
    
    for p_index, curr_stolen_descriptors in all_descriptors.items():
        
        curr_stolen_descriptors = np.array(curr_stolen_descriptors)

        # if there are not enough features, continue
        if found_painting_descriptor is None or len(found_painting_descriptor) <= k or curr_stolen_descriptors is None or len(curr_stolen_descriptors) <= k:
            best_match_index = -1
            continue
        

        matches_count[p_index] = matcher.get_good_matches_number(found_painting_descriptor, curr_stolen_descriptors, k)


    best_matches_indices = list(dict(sorted(matches_count.items(), key = lambda g: (g[1]), reverse = True)[:n_nearest]).keys())
    print(best_matches_indices)
    for index in best_matches_indices:
        if matches_count[index] < min_matches:
            best_matches_indices.remove(index)

    return best_matches_indices


class Flann:


    def __init__(self):
        
         # FLANN BASED MATCHER parameters, recommended by
         # https://opencv-python-tutroals.readthedocs.io/en/latest/py_tutorials/py_feature2d/py_matcher/py_matcher.html
         # c++ implementation: https://github.com/flann-lib/flann/blob/master/src/cpp/flann/algorithms/lsh_index.h

        FLANN_INDEX_LSH = 6
        index_params = dict(algorithm = FLANN_INDEX_LSH,
                    table_number = 6, # the number of hash tables to use
                    key_size = 12, # length of key in hash tables    
                    multi_probe_level = 1 # number of levels to use in multiprobe
                    )


        # number of times the trees in the index will be recursively traverse,
        # higher = higher precision, but takes more time
        search_params = dict(checks=50)  


        self.matcher = cv2.FlannBasedMatcher(index_params, search_params) 
    
    def get_good_matches_number(self, desc1, desc2, k):
        
        matches = self.matcher.knnMatch(desc1, desc2, k)
        ok_matches_num = 0

        # ratio test 
        for i, candidates in enumerate(matches):
            
            if (len(candidates)<2):
                continue # not enough features for comparison

            m, n = candidates    
            if m.distance < 0.7*n.distance:
                ok_matches_num = ok_matches_num + 1 
        
        return ok_matches_num


class Brute:


    def __init__(self, crossCheck = True):
        
        self.matcher = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck = crossCheck)
        self.crossCheck = True

    def get_good_matches_number(self, desc1, desc2):
        
        ok_matches_num = 0


        if not self.crossCheck:

            matches = self.matcher.knnMatch(desc1, desc2, 2) 

            # ratio test 
            for i, candidates in enumerate(matches):
                
                if (len(candidates)<2):
                    continue # not enough features for comparison

                m, n = candidates    
                if m.distance < 0.7*n.distance:
                    ok_matches_num = ok_matches_num + 1 
            
            return ok_matches_num
        
        else:

            matches = self.matcher.match(desc1, desc2)
            
            return len(matches)
