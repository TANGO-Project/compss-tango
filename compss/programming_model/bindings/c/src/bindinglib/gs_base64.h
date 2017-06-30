/*
 *  Copyright 2.1.rc17062-2.1.rc17067 Barcelona Supercomputing Center (www.bsc.es)
 *
 *  Licensed under the Apache License, Version 2.1.rc1706 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.1.rc1706
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
#ifndef _BSD_BASE64_H
#define _BSD_BASE64_H

#ifdef __cplusplus
extern "C" {
#endif

int gs_b64_ntop(char const *src, size_t srclength, char *target, size_t targsize);
int gs_b64_pton(char const *src, char *target, size_t targsize);

#ifdef __cplusplus
}
#endif

#endif /* _BSD_BASE64_H */
