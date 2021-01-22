# Working with environments


## Initial setup

Run this command to install the environment for the first time. 

```console
conda env create -f environment.yml
```

## Updating the environment

Run this command after you pull new version of the project. 

```console
conda env update -f local.yml --prune
```

## Setup jupyter notebook to work with the environment

If you want the environment to be displayed in jupyter, run:

```console
python -m ipykernel install --user --name=ENV_NAME
```

**Replace ENV_NAME** with the environment name! (for example "SAR")

Then you should be able to select **Kernel -> Change Kernel -> ENV_NAME** in jupyter.

## Pushing changes

Before pushing new version of the project, make sure to export the environment.

```console
conda env export > environment.yml
```