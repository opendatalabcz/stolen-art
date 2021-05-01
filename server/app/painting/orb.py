import cv2



class ORB:


    def __init__(self, n_features=500, scaleFactor=1.2, nlevels=8, edgeThreshold=31, firstLevel=0, WTA_k=2,
                        scoreType=cv2.ORB_HARRIS_SCORE, patchSize=31, fastThreshold=20):
        
        self.orb = cv2.ORB_create(nfeatures=n_features)

    def load_and_preprocess(self, path):

        image = cv2.imread(path)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        return image


    def detect_and_compute_from_paths(self, paths, return_keypoints=True):

        orb_results = []

        for path in paths:

            image = self.load_and_preprocess(path)
            res = self.detect_and_compute(image, return_keypoints=return_keypoints)
            orb_results.append(res)

        return orb_results

    def detect_and_compute(self, image, return_keypoints=True):

        keypoints, descriptors = self.orb.detectAndCompute(image, None)

        if return_keypoints:
            return keypoints, descriptors
        else: 
            return descriptors
        