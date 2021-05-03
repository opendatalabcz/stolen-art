# Data Augmentation Notebook

This notebook can be used for augmenting the downloaded paintings.
It augments all paintings from a given directory and saves them to a new directory with *_aug* postfix.
It also contains a function for before/after visualisation.


## Usage

1. Specify the directory which contains the paintings to be augmented:
```python
to_augment_dir = "../datasets/example_oil/"
```  
2. Set the augmenters_list variable to contain the augmenters you want to use:
```python
augmenters_list = [
    iaa.GaussianBlur(sigma=(0, 5.0)),
    iaa.Sometimes(0.50,
                iaa.JpegCompression(compression=(0, 30)) ),
] 
``` 
3. (Optional) Visualise the changes
```python
compare_orig_aug(paintings, augmented, limit=2)
```
4. Run all cells.