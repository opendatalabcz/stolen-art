from django.db import models

# Create your models here.



class Painting(models.Model):
    """Tag to be used for a recipe"""
    name = models.CharField(max_length=255)
   

    def __str__(self):
        return self.name