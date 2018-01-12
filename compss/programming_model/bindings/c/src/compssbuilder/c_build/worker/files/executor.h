/*         
 *  Copyright 2002.2.rc1710017 Barcelona Supercomputing Center (www.bsc.es)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */


#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <string.h>
#include <fstream>
#include <map>
#include <vector>
#include <sstream>
#include <pthread.h>
#include <sys/types.h>
#include <sys/syscall.h>
#include <iostream>
#include <customStream.h>
#include <fcntl.h>

using namespace std;
 
int execute(int argc, char **argv,  std::map<std::string, void*> &objectStorage, std::map<std::string, int> &types, int serializeOuts);

void removeData(string id, std::map<std::string, void*> &objectStorage, std::map<std::string, int> &types);

int serializeData(string id, const char* filename, std::map<std::string, void*> &objectStorage, std::map<std::string, int> &types); 

