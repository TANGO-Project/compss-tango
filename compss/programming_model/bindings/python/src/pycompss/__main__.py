
"""
PyCOMPSs Binding - Runnable as module
=====================================
Provides the current functionality to be run as a module.
e.g. python -m pycompss run -dgt myapp.py
"""
import sys
import argparse
from subprocess import Popen

RUN_TAG = 'run'
ENQUEUE_TAG = 'enqueue'
RUN_EXECUTABLE = 'runcompss'
ENQUEUE_EXECUTABLE = 'enqueue_compss'

def setup_parser():
    """
    Argument parser.
        * Argument defining run for runcompss or enqueue for enqueue_compss.
        * The rest of the arguments as a list
    :return: the parser
    """
    parser = argparse.ArgumentParser(prog='python -m pycompss')
    parser.add_argument('action', choices=[RUN_TAG, ENQUEUE_TAG], help='Execution mode: \'run\' for launching an execution and \'enqueue\' for submitting a job to the queuing system')
    parser.add_argument('args', nargs='*', help='COMPSs and application arguments (check \'runcompss\' or \'enqueue_compss\' commands help).')
    return parser


def run(cmd):
    p = Popen(cmd, stdout=sys.stdout, stderr=sys.stderr)
    p.communicate()


def main():
    """
    Main method.
    """
    parser = setup_parser()
    # Using parse_known_args allows us to get the rest of the arguments
    # as the 'params' list
    args, params = parser.parse_known_args()
    if args.action == RUN_TAG:
        cmd = [RUN_EXECUTABLE] + params
        run(cmd)
    elif args.action == ENQUEUE_TAG:
        cmd = [ENQUEUE_EXECUTABLE] + params
        run(cmd)
    else:
        # Unreachable code. Argparse already filters the two previous tags.
        print "ERROR: Undefined action."


if __name__=='__main__':
    main()