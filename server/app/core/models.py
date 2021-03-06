import uuid
import os

from django.db import models
from django.contrib.postgres.fields import ArrayField

def painting_image_file_path(instance, filename):
    """Generate file path for new painting image"""
    ext = filename.split('.')[-1]
    filename = f'{uuid.uuid4()}.{ext}'
    return os.path.join('uploads/painting/', filename)

class Painting(models.Model):
    """Image with name"""
    name = models.CharField(max_length=255)
    image = models.ImageField(upload_to=painting_image_file_path) 
   

    def __str__(self):
        return self.name

class PaintingDescriptors(models.Model):
    """Descriptors of a painting"""

    painting = models.ForeignKey(Painting, on_delete=models.CASCADE)

    descriptors = ArrayField(
        ArrayField(
            models.IntegerField(),
            size=32,
        ),
    )

   

    def __str__(self):
        return self.name
