
from django.core.management.base import BaseCommand

import sys
sys.path.append("/app/painting/")
from painting import data_loader

class Command(BaseCommand):


    def add_arguments(self, parser):

        # Positional arguments
        parser.add_argument('directory_name', nargs='+', type=str)


    def handle(self, *args, **kwargs):
        """Handle the command"""
        #self.stdout.write(type(kwargs['directory_name']))
        paintings_to_process_dir = kwargs['directory_name'][0]
        self.stdout.write(f"I got the command, will try to process paintings at {paintings_to_process_dir}:")

        loader = data_loader.DataLoader
        loader.process_paintings(paintings_to_process_dir)
        