from rest_framework import viewsets, mixins

from core.models import Painting

from painting import serializers

from django.core import serializers as core_serializers


from painting.pipelines import process_dir_to_array



class PaintingViewSet(viewsets.GenericViewSet,
                 mixins.ListModelMixin,
                 mixins.CreateModelMixin):
    """Manage Paintings in the database"""
    queryset = Painting.objects.all()
    serializer_class = serializers.PaintingSerializer

    def get_queryset(self):

        image = self.request.query_params.get('image')
        if image:
            print("image yes")
        else:
            print("image no")

        return self.queryset

    def perform_create(self, serializer):
        """Create a new painting"""

        serializer.save()


from rest_framework.response import Response
import time


class SearchPaintingViewSet(viewsets.GenericViewSet,
                 mixins.ListModelMixin
                 ):
    """Manage Paintings in the database"""
    queryset = Painting.objects.none()
    serializer_class = serializers.ImageSearchParamsSerializer

    def get_queryset(self):
        
        return self.queryset

    def create(self, request, *args, **kwargs):

        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        # how many most similar paintings to find
        k = request.POST.get('k')

        # the painting uploaded by user
        image = request.FILES['image']
        
        print(type(image))
        print(k)
        
        # testing custom return

        ok_ids = [5,6,7]
        time.sleep(3)

        mokdata = Painting.objects.filter(pk__in=ok_ids).values()

        return Response(mokdata)
