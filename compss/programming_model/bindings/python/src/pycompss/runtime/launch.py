#
#  Copyright 2.1.rc17062-2.1.rc17067 Barcelona Supercomputing Center (www.bsc.es)
#
#  Licensed under the Apache License, Version 2.1.rc1706 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.1.rc1706
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
'''@author: etejedor
@author: fconejer
@author: srodrig1

PyCOMPSs Binding - Launch
=========================
  This file contains the __main__ method.
  It is called from pycompssext script with the user and environment parameters.
'''
import os
import sys
import logging
import traceback
import pycompss.runtime.binding as binding
from tempfile import mkdtemp
from pycompss.api.api import compss_start, compss_stop
from pycompss.runtime.binding import get_log_path
from pycompss.util.logs import init_logging
from pycompss.util.jvm_parser import convert_to_dict
from pycompss.util.serializer import SerializerException
from pycompss.util.object_properties import is_module_available
from pycompss.util.optional_modules import show_optional_module_warnings
from random import randint

app_path = None

def get_logging_cfg_file(log_level):
    logging_cfg_file = 'logging.json'
    cfg_files = {
        'debug': 'logging.json.debug',
        'info': 'logging.json.off',
        'off': 'logging.json.off'
    }
    if log_level in cfg_files:
        logging_cfg_file = cfg_files[log_level]
    return logging_cfg_file

def parse_arguments():
    import argparse
    parser = argparse.ArgumentParser(description='PyCOMPSs application launcher')
    parser.add_argument('log_level', help='Logging level [debug|info|off]')
    parser.add_argument('object_conversion', help='Object_conversion [true|false]')
    parser.add_argument('storage_configuration', help='Storage configuration [null|*]')
    parser.add_argument('app_path', help='Application path')
    parser.add_argument('app_args', nargs='*', help='Application arguments')
    return parser.parse_args()

def main():
    ''' General call:
    python $PYCOMPSS_HOME/pycompss/runtime/launch.py $log_level $PyObject_serialize $storageConf $fullAppPath $application_args
    '''
    global app_path

    # Start the runtime, see bindings commons
    compss_start()

    # See parse_arguments, defined above
    args = parse_arguments()

    # Get log_level
    log_level = args.log_level

    # Get object_conversion boolean
    binding.object_conversion = args.object_conversion == 'true'

    # Get storage configuration at master
    storage_conf = args.storage_configuration
    persistent_storage = False
    if storage_conf != 'null':
        persistent_storage = True
        from storage.api import init as init_storage
        from storage.api import finish as finish_storage

    # Remove non-application arguments from the command line argument list
    sys.argv = [args.app_path] + args.app_args

    # Get application execution path
    app_path = args.app_path

    binding_log_path = get_log_path()
    log_path = os.path.join(os.getenv('IT_HOME'), 'Bindings', 'python', 'log')
    binding.temp_dir = mkdtemp(prefix='pycompss', dir=os.path.join(binding_log_path, 'tmpFiles/'))

    logging_cfg_file = get_logging_cfg_file(log_level)

    init_logging(os.path.join(log_path, logging_cfg_file), binding_log_path)
    logger = logging.getLogger('pycompss.runtime.launch')

    # Get JVM options
    jvm_opts = os.environ['JVM_OPTIONS_FILE']
    opts = convert_to_dict(jvm_opts)
    # storage_conf = opts.get('-Dit.storage.conf')

    try:
        logger.debug('--- START ---')
        logger.debug('PyCOMPSs Log path: %s' % binding_log_path)
        if persistent_storage:
            logger.debug('Storage configuration file: %s' % storage_conf)
            init_storage(config_file_path=storage_conf)
        show_optional_module_warnings()
        execfile(app_path, globals())    # MAIN EXECUTION
        if persistent_storage:
            finish_storage()
        logger.debug('--- END ---')
    except SystemExit as e:
            if e.code != 0:
                print '[ ERROR ]: User program ended with exitcode %s.'%e.code
                print '\t\tShutting down runtime...'
    except SerializerException:
        # If an object that can not be serialized has been used as a parameter.
        exc_type, exc_value, exc_traceback = sys.exc_info()
        lines = traceback.format_exception(exc_type, exc_value, exc_traceback)
        for line in lines:
            if app_path in line:
                print '[ ERROR ]: In: ' + line,
    finally:
        compss_stop()
        sys.stdout.flush()
        sys.stderr.flush()

    # --- Execution finished ---


#################################################
# For external execution
#################################################

# Version 4.0
def launch_pycompss_application(app, func, args=[], kwargs={},
                log_level='off',
                o_c=False,
                debug=False,
                graph=False,
                trace=False,
                monitor=None,
                project_xml=None,
                resources_xml=None,
                summary=False,
                taskExecution='compss',
                storageConf=None,
                taskCount=50,
                appName=None,
                uuid=None,
                baseLogDir=None,
                specificLogDir=None,
                extraeCfg=None,
                comm='NIO',
                conn='integratedtoolkit.connectors.DefaultSSHConnector',
                masterName='',
                masterPort='43000',
                scheduler='integratedtoolkit.scheduler.resourceEmptyScheduler.ResourceEmptyScheduler',
                jvmWorkers='-Xms1024m,-Xmx1024m,-Xmn400m',
                obj_conv=False,
                mmap_files=False
                ):
    global app_path
    launchPath = os.path.dirname(os.path.abspath(__file__))
    # it_home = launchPath without the last 4 folders (Bindings/python/pycompss/runtime)
    it_home = os.path.sep.join(launchPath.split(os.path.sep)[:-4])

    # Grab the existing PYTHONPATH and CLASSPATH values
    pythonpath = os.environ['PYTHONPATH']
    classpath = os.environ['CLASSPATH']

    # Enable/Disable object to string conversion
    binding.object_conversion = obj_conv
    # Enable/Disable the use of mmap when serializing.
    binding.mmap_file_storage = mmap_files

    # Get the filename and its path.
    file_name = os.path.splitext(os.path.basename(app))[0]
    cp = os.path.dirname(app)

    # Build a dictionary with all variables needed for initializing the runtime.
    config = {}
    config['it_home'] = it_home
    config['debug'] = debug
    if project_xml is None:
        config['project_xml'] = it_home + os.path.sep + 'Runtime/configuration/xml/projects/default_project.xml'
    else:
        config['project_xml'] = project_xml
    if resources_xml is None:
        config['resources_xml'] = it_home + os.path.sep + 'Runtime/configuration/xml/resources/default_resources.xml'
    else:
        config['resources_xml'] = resources_xml
    config['summary'] = summary
    config['taskExecution'] = taskExecution
    config['storageConf'] = storageConf
    config['taskCount'] = taskCount
    if appName is None:
        config['appName'] = file_name
    else:
        config['appName'] = appName
    config['uuid'] = uuid
    config['baseLogDir'] = baseLogDir
    config['specificLogDir'] = specificLogDir
    config['graph'] = graph
    config['monitor'] = monitor
    config['trace'] = trace
    config['extraeCfg'] = extraeCfg
    config['comm'] = comm
    config['conn'] = conn
    config['masterName'] = masterName
    config['masterPort'] = masterPort
    config['scheduler'] = scheduler
    config['cp'] = cp
    config['classpath'] = classpath
    config['jvmWorkers'] = jvmWorkers
    config['pythonPath'] = pythonpath

    initialize_compss(config)

    # Runtime start
    compss_start()

    # Configure logging
    app_path = app
    log_path = get_log_path()
    if debug:
        # DEBUG
        init_logging(it_home + '/Bindings/python/log/logging.json.debug', log_path)
    else:
        # NO DEBUG
        init_logging(it_home + '/Bindings/python/log/logging.json', log_path)
    logger = logging.getLogger("pycompss.runtime.launch")

    logger.debug('--- START ---')
    logger.debug('PyCOMPSs Log path: %s' % log_path)
    saved_argv = sys.argv
    sys.argv = args
    # Execution:
    if func is None or func == '__main__':
        result = execfile(app)
    else:
        import imp
        imported_module = imp.load_source(file_name, app)
        methodToCall = getattr(imported_module, func)
        result = methodToCall(*args, **kwargs)
    # Recover the system arguments
    sys.argv = saved_argv
    logger.debug('--- END ---')

    compss_stop()

    return result


def initialize_compss(config):
    '''Creates the initialization files for the runtime start (java options file).
    Receives a dictionary (config) with the configuration parameters.
    WARNING!!! if new parameters are included in the runcompss launcher,
    they have to be considered in this configuration. Otherwise, the runtime will not start.
    * Current required parameters:
        - 'it_home'        = <String>       = COMPSs installation path
        - 'debug'          = <Boolean>      = Enable/Disable debugging (True|False)
        - 'project_xml'    = <String>       = Specific project.xml path
        - 'resources_xml'  = <String>       = Specific resources.xml path
        - 'summary'        = <Boolean>      = Enable/Disable summary (True|False)
        - 'taskExecution'  = <String>       = Who performs the task execution (normally "compss")
        - 'storageConf'    = None|<String>  = Storage configuration file path
        - 'taskCount'      = <Integer>      = Number of tasks (for structure initialization purposes)
        - 'appName'        = <String>       = Application name
        - 'uuid'           = None|<String>  = Application UUID
        - 'baseLogDir'     = None|<String>  = Base log path
        - 'specificLogDir' = None|<String>  = Specific log path
        - 'graph'          = <Boolean>      = Enable/Disable graph generation
        - 'monitor'        = None|<Integer> = Disable/Frequency of the monitor
        - 'trace'          = <Boolean>      = Enable/Disable trace generation
        - 'extraeCfg'      = None|<String>  = Default extrae configuration/User specific extrae configuration
        - 'comm'           = <String>       = GAT/NIO
        - 'conn'           = <String>       = Connector (normally: integratedtoolkit.connectors.DefaultSSHConnector)
        - 'masterName'     = <String>       = Master node name
        - 'masterPort'     = <String>       = Master node port
        - 'scheduler'      = <String>       = Scheduler (normally: integratedtoolkit.scheduler.resourceEmptyScheduler.ResourceEmptyScheduler)
        - 'cp'             = <String>       = Application path
        - 'classpath'      = <String>       = CLASSPATH environment variable contents
        - 'pythonPath'     = <String>       = PYTHONPATH environment variable contents
        - 'jvmWorkers'     = <String>       = Worker's jvm configuration (example: "-Xms1024m,-Xmx1024m,-Xmn400m")
        - 'cpuAffinity'
        - 'gpuAffinity'
    :param config: Configuration parameters dictionary
    '''
    from tempfile import mkstemp
    fd, temp_path = mkstemp()
    jvm_options_file = open(temp_path, 'w')

    jvm_options_file.write('-XX:+PerfDisableSharedMem\n')
    jvm_options_file.write('-XX:-UsePerfData\n')
    jvm_options_file.write('-XX:+UseG1GC\n')
    jvm_options_file.write('-XX:+UseThreadPriorities\n')
    jvm_options_file.write('-XX:ThreadPriorityPolicy=42\n')
    if config['debug']:
        jvm_options_file.write('-Dlog4j.configurationFile=' + config['it_home'] + '/Runtime/configuration/log/COMPSsMaster-log4j.debug\n')  # DEBUG
    else:
        jvm_options_file.write('-Dlog4j.configurationFile=' + config['it_home'] + '/Runtime/configuration/log/COMPSsMaster-log4j\n')  # NO DEBUG
    jvm_options_file.write('-Dit.to.file=false\n')
    jvm_options_file.write('-Dit.project.file=' + config['project_xml'] + '\n')
    jvm_options_file.write('-Dit.resources.file=' + config['resources_xml'] + '\n')
    jvm_options_file.write(
        '-Dit.project.schema=' + config['it_home'] + '/Runtime/configuration/xml/projects/project_schema.xsd\n')
    jvm_options_file.write(
        '-Dit.resources.schema=' + config['it_home'] + '/Runtime/configuration/xml/resources/resources_schema.xsd\n')
    jvm_options_file.write('-Dit.lang=python\n')
    if config['summary']:
        jvm_options_file.write('-Dit.summary=true\n')
    else:
        jvm_options_file.write('-Dit.summary=false\n')
    jvm_options_file.write('-Dit.task.execution=' + config['taskExecution'] + '\n')
    if config['storageConf'] is None:
        jvm_options_file.write('-Dit.storage.conf=null\n')
    else:
        jvm_options_file.write('-Dit.storage.conf=' + config['storageConf'] + '\n')

    jvm_options_file.write('-Dit.core.count=' + str(config['taskCount']) + '\n')
    jvm_options_file.write('-Dit.appName=' + config['appName'] + '\n')

    if config['uuid'] is None:
        myUuid = str(randint(0, 1000))
    else:
        myUuid = config['uuid']

    jvm_options_file.write('-Dit.uuid=' + myUuid + '\n')

    if config['baseLogDir'] is None:
        # it will be within $HOME/.COMPSs
        jvm_options_file.write('-Dit.baseLogDir=\n')
    else:
        jvm_options_file.write('-Dit.baseLogDir=' + config['baseLogDir'] + '\n')

    if config['specificLogDir'] is None:
        jvm_options_file.write('-Dit.specificLogDir=\n')
    else:
        jvm_options_file.write('-Dit.specificLogDir=' + config['specificLogDir'] + '\n')

    jvm_options_file.write('-Dit.appLogDir=/tmp/' + myUuid + '/\n')

    if config['graph']:
        jvm_options_file.write('-Dit.graph=true\n')
    else:
        jvm_options_file.write('-Dit.graph=false\n')

    if config['monitor'] is None:
        jvm_options_file.write('-Dit.monitor=0\n')
    else:
        jvm_options_file.write('-Dit.monitor=' + str(config['monitor']) + '\n')

    if not config['trace'] or config['trace'] == 0:
        jvm_options_file.write('-Dit.tracing=0' + '\n')
    elif config['trace'] == 1:
        jvm_options_file.write('-Dit.tracing=1\n')
        os.environ['EXTRAE_CONFIG_FILE'] = config['it_home'] + '/Runtime/configuration/xml/tracing/extrae_basic.xml'
    elif config['trace'] == 2:
        jvm_options_file.write('-Dit.tracing=2\n')
        os.environ['EXTRAE_CONFIG_FILE'] = config['it_home'] + '/Runtime/configuration/xml/tracing/extrae_advanced.xml'
    else:
        jvm_options_file.write('-Dit.tracing=0' + '\n')

    if config['extraeCfg'] is None:
        jvm_options_file.write('-Dit.extrae.file=null\n')
    else:
        jvm_options_file.write('-Dit.extrae.file=' + config['extraeCfg'] + '\n')

    if config['comm'] == 'GAT':
        jvm_options_file.write('-Dit.comm=integratedtoolkit.gat.master.GATAdaptor\n')
    else:
        jvm_options_file.write('-Dit.comm=integratedtoolkit.nio.master.NIOAdaptor\n')

    jvm_options_file.write('-Dit.conn=' + config['conn'] + '\n')
    jvm_options_file.write('-Dit.masterName=' + config['masterName'] + '\n')
    jvm_options_file.write('-Dit.masterPort=' + config['masterPort'] + '\n')
    jvm_options_file.write('-Dit.scheduler=' + config['scheduler'] + '\n')
    jvm_options_file.write('-Dgat.adaptor.path=' + config['it_home'] + '/Dependencies/JAVA_GAT/lib/adaptors\n')
    jvm_options_file.write('-Dit.gat.broker.adaptor=sshtrilead\n')
    jvm_options_file.write('-Dit.gat.file.adaptor=sshtrilead\n')
    jvm_options_file.write('-Dit.worker.cp=' + config['cp'] + ':' + config['classpath'] + '\n')
    jvm_options_file.write('-Dit.worker.jvm_opts=' + config['jvmWorkers'] + '\n')
    jvm_options_file.write('-Dit.worker.cpu_affinity=' + config['cpuAffinity'] + '\n')
    jvm_options_file.write('-Dit.worker.gpu_affinity=' + config['gpuAffinity'] + '\n')
    jvm_options_file.write('-Djava.class.path=' + config['cp'] + ':' + config['it_home'] + '/Runtime/compss-engine.jar:' + config['classpath'] + '\n')
    jvm_options_file.write('-Dit.worker.pythonpath=' + config['cp'] + ':'+ config['pythonPath'] + '\n')
    jvm_options_file.close()
    os.close(fd)
    os.environ['JVM_OPTIONS_FILE'] = temp_path

    # print "Uncomment if you want to check the configuration file path."
    # print "JVM_OPTIONS_FILE", temp_path


# Version 3.0
# ==============================================================================
# def launch_pycompss_application(app, func, args=[], kwargs={},
#                                 classpath='.',
#                                 debug=False,
#                                 graph=False,
#                                 trace=False,
#                                 project_xml=os.environ['IT_HOME'] + '/Runtime/configuration/xml/projects/default_project.xml',
#                                 resources_xml=os.environ['IT_HOME'] + '/Runtime/configuration/xml/resources/default_resources.xml',
#                                 comm='NIO',
#                                 obj_conv=False,
#                                 mmap_files=False):
#     global app_path
#     it_home = os.environ['IT_HOME']
#     pythonpath = os.environ['PYTHONPATH']
#     e_classpath = os.environ['CLASSPATH']
#
#     binding.object_conversion = obj_conv
#     binding.mmap_file_storage = mmap_files
#
#     # dirs = app.split(os.path.sep)
#     file_name = os.path.splitext(os.path.basename(app))[0]
#     cp = os.path.dirname(app)
#
#     from tempfile import mkstemp
#     fd, temp_path = mkstemp()
#     jvm_options_file = open(temp_path, 'w')
#
#     jvm_options_file.write('-Djava.class.path=' + it_home + '/Runtime/compss-engine.jar:' + e_classpath
#                            + ':' + cp + ':' + classpath + '\n')
#     if debug:
#         jvm_options_file.write('-Dlog4j.configuration=' + it_home
#                                 + '/Runtime/configuration/log/COMPSsMaster-log4j.debug\n')   # DEBUG
#     else:
#         jvm_options_file.write('-Dlog4j.configuration=' + it_home
#                                 + '/Runtime/configuration/log/COMPSsMaster-log4j\n')       # NO DEBUG
#     jvm_options_file.write('-Dit.to.file=false\n')
#     jvm_options_file.write('-Dit.lang=python\n')
#     jvm_options_file.write('-Dit.project.file=' + project_xml + '\n')
#     jvm_options_file.write('-Dit.resources.file=' + resources_xml + '\n')
#     jvm_options_file.write('-Dit.project.schema=' + it_home + '/Runtime/configuration/xml/projects/project_schema.xsd\n')
#     jvm_options_file.write('-Dit.resources.schema=' + it_home + '/Runtime/configuration/xml/resources/resources_schema.xsd\n')
#     # jvm_options_file.write('-Dit.appName=' + app.__name__ + '\n')
#     jvm_options_file.write('-Dit.appName=' + file_name + '\n')
#     jvm_options_file.write('-Dit.appLogDir=/tmp/\n')
#     if graph:
#         jvm_options_file.write('-Dit.graph=true\n')
#     else:
#         jvm_options_file.write('-Dit.graph=false\n')
#     jvm_options_file.write('-Dit.monitor=60000\n')
#     if trace:
#         jvm_options_file.write('-Dit.tracing=true\n')
#     else:
#         jvm_options_file.write('-Dit.tracing=false\n')
#     jvm_options_file.write('-Dit.core.count=50\n')
#     jvm_options_file.write('-Dit.worker.cp=' + pythonpath + ':' + e_classpath + ':' + cp + ':' + classpath + '\n')
#     if comm == 'GAT':
#         jvm_options_file.write('-Dit.comm=integratedtoolkit.gat.master.GATAdaptor\n')
#     else:
#         jvm_options_file.write('-Dit.comm=integratedtoolkit.nio.master.NIOAdaptor\n')
#     jvm_options_file.write('-Dgat.adaptor.path=' + it_home + '/Dependencies/JAVA_GAT/lib/adaptors\n')
#     jvm_options_file.write('-Dit.gat.broker.adaptor=sshtrilead\n')
#     jvm_options_file.write('-Dit.gat.file.adaptor=sshtrilead\n')
#
#     jvm_options_file.close()
#     os.close(fd)
#     os.environ['JVM_OPTIONS_FILE'] = temp_path
#
#     # Runtime start
#     compss_start()
#
#     # Configure logging
#     app_path = app
#     log_path = get_log_path()
#     if debug:
#         # DEBUG
#         init_logging(os.getenv('IT_HOME') + '/Bindings/python/log/logging.json.debug', log_path)
#     else:
#         # NO DEBUG
#         init_logging(os.getenv('IT_HOME') + '/Bindings/python/log/logging.json', log_path)
#     logger = logging.getLogger("pycompss.runtime.launch")
#
#     logger.debug("--- START ---")
#     logger.debug("PyCOMPSs Log path: %s" % log_path)
#     saved_argv = sys.argv
#     sys.argv = args
#     # Execution:
#     if func is None or func == '__main__':
#         result = execfile(app)
#     else:
#         import imp
#         imported_module = imp.load_source(file_name, app)
#         methodToCall = getattr(imported_module, func)
#         result = methodToCall(*args, **kwargs)
#     # Recover the system arguments
#     sys.argv = saved_argv
#     logger.debug("--- END ---")
#
#     compss_stop()
#
#     return result
# ==============================================================================

# Version 2.0
# ==============================================================================
# def launch_pycompss_module(app, func, args, kwargs): # explicit parameter passing
#     global app_path
#     it_home = os.environ['IT_HOME']
#     pythonpath = os.environ['PYTHONPATH']
#     classpath = os.environ['CLASSPATH']
#
#     dirs = app.split(os.path.sep)
#     file_name = os.path.splitext(os.path.basename(app))[0]
#     cp = os.path.dirname(app)
#
#     from tempfile import mkstemp
#     fd, temp_path = mkstemp()
#     jvm_options_file = open(temp_path, 'w')
#
#     jvm_options_file.write('-Djava.class.path=' + it_home + '/Runtime/compss-engine.jar:' + classpath + ':' + cp + '\n')
#     jvm_options_file.write('-Dlog4j.configuration=' + it_home + '/Runtime/configuration/log/it-log4j.debug\n')   # DEBUG
#     #jvm_options_file.write('-Dlog4j.configuration=' + it_home + '/Runtime/configuration/log/it-log4j\n')          # NO DEBUG
#     jvm_options_file.write('-Dit.to.file=false\n')
#     jvm_options_file.write('-Dit.lang=python\n')
#     jvm_options_file.write('-Dit.project.file=' + it_home + '/Runtime/configuration/xml/projects/project.xml\n')
#     jvm_options_file.write('-Dit.resources.file=' + it_home + '/Runtime/configuration/xml/resources/resources.xml\n')
#     jvm_options_file.write('-Dit.project.schema=' + it_home + '/Runtime/configuration/xml/projects/project_schema.xsd\n')
#     jvm_options_file.write('-Dit.resources.schema=' + it_home + '/Runtime/configuration/xml/resources/resource_schema.xsd\n')
#     #jvm_options_file.write('-Dit.appName=' + app.__name__ + '\n')
#     jvm_options_file.write('-Dit.appName=' + file_name + '\n')
#     jvm_options_file.write('-Dit.appLogDir=/tmp/\n')
#     jvm_options_file.write('-Dit.graph=false\n')
#     jvm_options_file.write('-Dit.monitor=60000\n')
#     jvm_options_file.write('-Dit.tracing=false\n')
#     jvm_options_file.write('-Dit.core.count=50\n')
#     jvm_options_file.write('-Dit.worker.cp=' + pythonpath + ':' + classpath + ':' + cp +'\n')
#     jvm_options_file.write('-Dit.comm=integratedtoolkit.gat.master.GATAdaptor\n') # integratedtoolkit.nio.master.NIOAdaptor
#     jvm_options_file.write('-Dgat.adaptor.path=' + it_home + '/Dependencies/JAVA_GAT/lib/adaptors\n')
#     jvm_options_file.write('-Dit.gat.broker.adaptor=sshtrilead\n')
#     jvm_options_file.write('-Dit.gat.file.adaptor=sshtrilead\n')
#
#     jvm_options_file.close()
#     os.close(fd)
#     os.environ['JVM_OPTIONS_FILE'] = temp_path
#
#     # Runtime start
#     compss_start()
#
#     # Configure logging
#     app_path = app
#     log_path = get_log_path()
#     init_logging(os.getenv('IT_HOME') + '/Bindings/python/log/logging.json.debug', log_path)   # 1.3 DEBUG
#     #init_logging(os.getenv('IT_HOME') + '/Bindings/python/log/logging.json', log_path)        # 1.3 NO DEBUG
#     logger = logging.getLogger("pycompss.runtime.launch")
#
#     logger.debug("--- START ---")
#     logger.debug("PyCOMPSs Log path: %s" % log_path)
#     saved_argv = sys.argv
#     sys.argv = args
#     # Execution:
#     if func is None or func == '__main__':
#         execfile(app)
#     else:
#         import imp
#         imported_module = imp.load_source(file_name, app)
#         methodToCall = getattr(imported_module, func)
#         result = methodToCall(*args, **kwargs)
#     # Recover the system arguments
#     sys.argv = saved_argv
#     logger.debug("--- END ---")
#
#     compss_stop()
#
#     return result
# ==============================================================================

# Version 1.0
# ==============================================================================
# def launch_pycompss_module(app, func, args):   # use the sys.arg for parameter passing
#     global app_path
#     it_home = os.environ['IT_HOME']
#     pythonpath = os.environ['PYTHONPATH']
#     classpath = os.environ['CLASSPATH']
#
#     dirs = app.split(os.path.sep)
#     file_name = os.path.splitext(os.path.basename(app))[0]
#     cp = os.path.dirname(app)
#
#     from tempfile import mkstemp
#     fd, temp_path = mkstemp()
#     jvm_options_file = open(temp_path, 'w')
#
#     jvm_options_file.write('-Djava.class.path=' + it_home + '/compss-engine.jar:' + classpath + ':' + cp + '\n')
#     #jvm_options_file.write('-Dlog4j.configuration=' + it_home + '/configuration/log/it-log4j.debug\n')   # DEBUG
#     jvm_options_file.write('-Dlog4j.configuration=' + it_home + '/configuration/log/it-log4j\n')          # NO DEBUG
#     jvm_options_file.write('-Dit.to.file=false\n')
#     jvm_options_file.write('-Dit.lang=python\n')
#     jvm_options_file.write('-Dit.project.file=' + it_home + '/configuration/xml/projects/project.xml\n')
#     jvm_options_file.write('-Dit.resources.file=' + it_home + '/configuration/xml/resources/resources.xml\n')
#     jvm_options_file.write('-Dit.project.schema=' + it_home + '/configuration/xml/projects/project_schema.xsd\n')
#     jvm_options_file.write('-Dit.resources.schema=' + it_home + '/configuration/xml/resources/resource_schema.xsd\n')
#     #jvm_options_file.write('-Dit.appName=' + app.__name__ + '\n')
#     jvm_options_file.write('-Dit.appName=' + file_name + '\n')
#     jvm_options_file.write('-Dit.appLogDir=/tmp/\n')
#     jvm_options_file.write('-Dit.graph=false\n')
#     jvm_options_file.write('-Dit.monitor=60000\n')
#     jvm_options_file.write('-Dit.tracing=false\n')
#     jvm_options_file.write('-Dit.core.count=50\n')
#     jvm_options_file.write('-Dit.worker.cp=' + pythonpath + ':' + classpath + ':' + cp +'\n')
#     jvm_options_file.write('-Dit.comm=integratedtoolkit.gat.master.GATAdaptor\n') # integratedtoolkit.nio.master.NIOAdaptor
#     jvm_options_file.write('-Dgat.adaptor.path=' + it_home + '/../Dependencies/JAVA_GAT/lib/adaptors\n')
#     jvm_options_file.write('-Dit.gat.broker.adaptor=sshtrilead\n')
#     jvm_options_file.write('-Dit.gat.file.adaptor=sshtrilead\n')
#
#     jvm_options_file.close()
#     os.close(fd)
#     os.environ['JVM_OPTIONS_FILE'] = temp_path
#
#     # Runtime start
#     compss_start()
#
#     # Configure logging
#     app_path = app
#     log_path = get_log_path()
#     #init_logging(os.getenv('IT_HOME') + '/../Bindings/python/log/logging.json.debug', log_path) # 1.3 DEBUG
#     init_logging(os.getenv('IT_HOME') + '/../Bindings/python/log/logging.json', log_path)        # 1.3 NO DEBUG
#     logger = logging.getLogger("pycompss.runtime.launch")
#
#     logger.debug("--- START ---")
#     logger.debug("PyCOMPSs Log path: %s" % log_path)
#     saved_argv = sys.argv
#     sys.argv = args
#     # Execution:
#     if func is None or func == '__main__':
#         execfile(app)
#     else:
#         import imp
#         imported_module = imp.load_source(file_name, app)
#         methodToCall = getattr(imported_module, func) # con (*args, **kwargs) no va
#         result = methodToCall()
#     # Recover the system arguments
#     sys.argv = saved_argv
#     logger.debug("--- END ---")
#
#     compss_stop()
#
#     return result
# ==============================================================================


################################
# Deprecated - Use version 3.0 #
################################
def pycompss_launch(app, args, kwargs):    # UNIFIED PORTAL - HBP
    '''PyCOMPSs launch function - Debugging - UNIFIED PORTAL (HBP)

    This function enables to execute a pycompss application from parameters.
    Useful for PyCOMPSs binding debugging from Eclipse IDE.
    Useful for PyCOMPSs binding integration in HBP - Unified Portal.

    @param app: Application to execute.
    @param args: Arguments.
    @param kwargs: Arguments dictionary.

    @return: The execution result.
    '''
    import os
    it_home = os.environ['IT_HOME']
    pythonpath = os.environ['PYTHONPATH']

    # TODO: update to PyCOMPSs 1.3
    from tempfile import mkstemp
    fd, temp_path = mkstemp()
    jvm_options_file = open(temp_path, 'w')
    jvm_options_file.write('-Djava.class.path=' + it_home + '/rt/compss-rt.jar\n')
    jvm_options_file.write('-Dlog4j.configuration=' + it_home + '/log/it-log4j\n')
    jvm_options_file.write('-Dgat.adaptor.path=' + it_home + '/../JAVA_GAT/lib/adaptors\n')
    jvm_options_file.write('-Dit.to.file=false\n')
    jvm_options_file.write('-Dit.gat.broker.adaptor=sshtrilead\n')
    jvm_options_file.write('-Dit.gat.file.adaptor=sshtrilead\n')
    jvm_options_file.write('-Dit.lang=python\n')
    jvm_options_file.write('-Dit.project.schema=' + it_home + '/xml/projects/project_schema.xsd\n')
    jvm_options_file.write('-Dit.resources.schema=' + it_home + '/xml/resources/resources_schema.xsd\n')
    jvm_options_file.write('-Dit.graph=false\n')
    jvm_options_file.write('-Dit.monitor=60000\n')
    jvm_options_file.write('-Dit.tracing=false\n')
    jvm_options_file.write('-Dit.core.count=50\n')
    jvm_options_file.write('-Dit.project.file=' + it_home + '/../infrastructure/project.xml\n')
    jvm_options_file.write('-Dit.resources.file=' + it_home + '/../infrastructure/resources.xml\n')
    jvm_options_file.write('-Dit.appName=' + app.__name__ + '\n')
    jvm_options_file.write('-Dit.worker.cp=' + pythonpath + '\n')
    jvm_options_file.close()
    os.close(fd)
    os.environ['JVM_OPTIONS_FILE'] = temp_path

    # from pycompss.api.api import compss_start, compss_stop
    compss_start()

    log_path = get_log_path()
    init_logging(os.getenv('IT_HOME') + '/../Bindings/python/log/logging.json', log_path)  # 1.3
    # init_logging(os.getenv('IT_HOME') + '/bindings/python/log/logging.json')            # 1.2

    result = app(*args, **kwargs)

    compss_stop()

    return result


''' This is the PyCOMPSs entry point
'''
if __name__ == '__main__':
    main()
