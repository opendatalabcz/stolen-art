from rest_framework import viewsets, mixins

from core.models import Painting, PaintingDescriptors

from painting import serializers

from django.core import serializers as core_serializers


from painting.pipelines import find_similar



class PaintingViewSet(viewsets.GenericViewSet,
                 mixins.ListModelMixin,
                 mixins.CreateModelMixin):
    """Manage Paintings in the database"""
    queryset = Painting.objects.all()
    serializer_class = serializers.PaintingSerializer

    def get_queryset(self):

        return self.queryset

    def perform_create(self, serializer):
        """Create a new painting"""

        serializer.save()


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
        
        similar_ids = find_similar(uploaded_image)
        
        
        # testing custom return

        ok_ids = [5,6,7]
        time.sleep(3)

        mokdata = Painting.objects.filter(pk__in=ok_ids).values()

        return Response(mokdata)


class DescriptorsViewSet(viewsets.GenericViewSet,
                 mixins.ListModelMixin):

    queryset = PaintingDescriptors.objects.order_by('?')[:5]
    serializer_class = serializers.PaintingDescriptorsSerializer

    def get_queryset(self):

        return self.queryset
