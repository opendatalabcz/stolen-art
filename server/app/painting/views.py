from rest_framework import viewsets, mixins

from core.models import Painting

from painting import serializers

from painting.pipelines import process_dir_to_array



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
        #process_dir_to_array("tate500")


