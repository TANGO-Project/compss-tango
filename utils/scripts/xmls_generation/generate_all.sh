#!/bin/bash

  usage() {
    echo "Usage: "
    echo "    $0 <projectFile> <resourcesFile> <remoteUser> List<workerName>"
    echo " "
  }

  ###########################################################
  # MAIN
  ###########################################################

  # Arguments
  echo "[GENERATE_ALL] [DEBUG] Parsing arguments"
  if [ $# -lt 3 ]; then
    echo "[GENERATE_ALL] [ERROR] Invalid number of arguments"
    usage
    exit 1
  fi
  projectFile="$1"
  resourcesFile="$2"
  remoteUser="$3"
  shift 3
  workers="$@"

  # Global variable
  scriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

  # Generate project
  echo "[GENERATE_ALL] [INFO] Generate project.xml file on ${projectFile}"
  ${scriptDir}/generate_project.sh "${projectFile}" "${remoteUser}" "${workers}"
  if [ $? -ne 0 ]; then
    echo "[GENERATE_ALL] [ERROR] Cannot generate project file"
    exit 1
  fi

  # Generate project
  echo "[GENERATE_ALL] [INFO] Generate resources.xml file on ${resourcesFile}"
  ${scriptDir}/generate_resources.sh "${resourcesFile}" "${remoteUser}" "${workers}"
  if [ $? -ne 0 ]; then
    echo "[GENERATE_ALL] [ERROR] Cannot generate resources file"
    exit 1
  fi

  # DONE
  echo "[GENERATE_ALL] [INFO] DONE"
  exit 0
 
