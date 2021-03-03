from rest_framework import serializers

from core.models import Painting


class PaintingSerializer(serializers.ModelSerializer):
    """Serializer for painting object"""

    class Meta:
        model = Painting
        fields = ('id', 'name', 'image')
        read_only_Fields = ('id',)


class ImageSearchParamsSerializer(serializers.Serializer):

    image = serializers.ImageField()
    k = serializers.IntegerField()