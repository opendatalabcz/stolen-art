
from django.core.management.base import BaseCommand

import sys
sys.path.append("/app/painting/")
from painting import data_loader
from painting.pipelines import process_dir_to_array
from django.core import management


class Command(BaseCommand):


    def add_arguments(self, parser):

        # Positional arguments
        parser.add_argument('directory_name', nargs='+', type=str)


    def handle(self, *args, **kwargs):
        """Handle the command"""
        
        management.call_command('wait_for_db')

        paintings_to_process_dir = kwargs['directory_name'][0]
        self.stdout.write(f"I got the command, will try to process paintings at {paintings_to_process_dir}:")

        process_dir_to_array(paintings_to_process_dir)
        
        