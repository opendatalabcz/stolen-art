import uuid
import os

from django.db import models

def painting_image_file_path(instance, filename):
    """Generate file path for new painting image"""
    ext = filename.split('.')[-1]
    filename = f'{uuid.uuid4()}.{ext}'
    return os.path.join('uploads/painting/', filename)

class Painting(models.Model):
    """Tag to be used for a recipe"""
    name = models.CharField(max_length=255)
    image = models.ImageField(upload_to=painting_image_file_path) 
   

    def __str__(self):
        return self.name

