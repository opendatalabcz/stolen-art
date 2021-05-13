from rest_framework import viewsets, mixins

from core.models import Painting, PaintingDescriptors

from painting import serializers

from django.core import serializers as core_serializers
from painting.data_saver import save_image_to_db

from painting.pipelines import find_similar
from app import settings


class PaintingViewSet(viewsets.GenericViewSet,
                 mixins.ListModelMixin,
                 mixins.CreateModelMixin):
    """Manage Paintings in the database"""
    queryset = Painting.objects.all()
    serializer_class = serializers.PaintingSerializer

    def get_queryset(self):

        self.queryset = self.queryset.all()
        return self.queryset

    def perform_create(self, serializer):
        """Create a new painting"""

        p = serializer.save()

        image = serializer.validated_data.get("image")        
        save_image_to_db(image, p)

from rest_framework.response import Response
import time


class SearchPaintingViewSet(viewsets.GenericViewSet,
                 mixins.ListModelMixin
                 ):
    

    queryset = Painting.objects.none()
    serializer_class = serializers.ImageSearchParamsSerializer

    def get_queryset(self):
        
        return self.queryset

    def create(self, request, *args, **kwargs):

        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        # how many most similar paintings to find
        k = serializer.validated_data.get("k")


        # the painting uploaded by user
        uploaded_image = serializer.validated_data.get("image")
        
        similar_ids = find_similar(uploaded_image, k)
        
        similar_paintings = Painting.objects.filter(pk__in=similar_ids).values()


        # MEDIA URL can change, so Django does not store it in DB, I need to prepend it myself
        for painting in similar_paintings:
            
            painting['image'] = "http://" + request.META['HTTP_HOST'] + settings.MEDIA_URL + painting['image']

        return Response(similar_paintings)


class DescriptorsViewSet(viewsets.GenericViewSet,
                 mixins.ListModelMixin):

    queryset = PaintingDescriptors.objects.order_by('?')[:1]
    serializer_class = serializers.PaintingDescriptorsSerializer

    def get_queryset(self):

        self.queryset = self.queryset.all()
        return self.queryset
