from django.urls import path, include
from rest_framework.routers import DefaultRouter

from painting import views


router = DefaultRouter()
router.register('paintings', views.PaintingViewSet)
router.register('search', views.SearchPaintingViewSet)

app_name = 'painting'

urlpatterns = [
    path('', include(router.urls))
]