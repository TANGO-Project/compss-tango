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
class InconsistentAction(Exception):
    '''
        Exception that is thrown when actions that whould lead
        to an inconsistent cache state are performed.
        For example, if our cache has a pair (ID_1, OBJ_1)
        then an InconsistentAction would consist to add
        a pair (ID_1, OBJ_2), since an unique ID -> OBJ
        is supposed (not necessarily the other way, though).
    '''
    pass
