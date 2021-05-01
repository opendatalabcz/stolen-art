from django.urls import path, include
from rest_framework.routers import DefaultRouter

from painting import views


router = DefaultRouter()
router.register('paintings', views.PaintingViewSet, 'paintings')
router.register('search', views.SearchPaintingViewSet, 'search')
#router.register('descriptors_sample', views.DescriptorsViewSet, 'descriptors')

app_name = 'painting'

urlpatterns = [
    path('', include(router.urls))
]