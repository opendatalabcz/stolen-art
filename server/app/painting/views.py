from rest_framework import viewsets, mixins

from core.models import Painting

from painting import serializers

# ZMENA - odstraneni auth veci

class PaintingViewSet(viewsets.GenericViewSet,
                 mixins.ListModelMixin,
                 mixins.CreateModelMixin):
    """Manage Paintings in the database"""
    queryset = Painting.objects.all()
    serializer_class = serializers.PaintingSerializer

    def get_queryset(self):
        """Return objects for the current authenticated user only"""
        return self.queryset

    def perform_create(self, serializer):
        """Create a new ingredient"""
        serializer.save()

