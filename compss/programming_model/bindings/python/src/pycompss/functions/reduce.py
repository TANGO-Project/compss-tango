#
#  Copyright 2002.2.rc1710017 Barcelona Supercomputing Center (www.bsc.es)
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
# 




"""
PyCOMPSs Functions: Reduce
===================================
    This file defines the common reduce functions.
"""


def mergeReduce(function, data):
    """
    Apply function cumulatively to the items of data,
    from left to right in binary tree structure, so as to
    reduce the data to a single value.
    :param function: function to apply to reduce data
    :param data: List of items to be reduced
    :return: result of reduce the data to a single value
    """
    from collections import deque
    q = deque(xrange(len(data)))
    dataNew = data[:]
    while len(q):
        x = q.popleft()
        if len(q):
            y = q.popleft()
            dataNew[x] = function(dataNew[x], dataNew[y])
            q.append(x)
        else:
            return dataNew[x]


def mergeNReduce(function, data):
    """
    Apply function cumulatively to the items of data,
    from left to right in n-tree structure, so as to
    reduce the data.
    :param function: function to apply to reduce data
    :param data: List of items to be reduced
    :return: List of results
    """
    from collections import deque
    q = deque(xrange(len(data)))
    numArgs = function.func_code.co_argcount
    while len(q) >= numArgs:
        args = [q.popleft() for _ in xrange(numArgs)]
        data[args[0]] = function(*[data[i] for i in args])
        q.append(args[0])
    else:
        args = [q.popleft() for _ in xrange(len(q))]
        return [data[i] for i in args]


def simpleReduce(function, data):
    """
    Apply function of two arguments cumulatively to the items
    of data, from left to right, so as to reduce the iterable
    to a single value.
    :param function: function to apply to reduce data
    :param data: List of items to be reduced
    :return: result of reduce the data to a single value
    """
    try:
        return reduce(function, data)
    except Exception, e:
        raise e
