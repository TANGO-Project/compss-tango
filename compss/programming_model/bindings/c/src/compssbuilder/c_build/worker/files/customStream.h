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
#include <iostream>
#include <fstream>
#include <pthread.h>
#include <sys/types.h>
#include <sys/syscall.h>
#include <mutex>
#define gettid() syscall(SYS_gettid)


using namespace std;

class customStream : public streambuf {

map<int, streambuf*> files;
streambuf* defaultBuf;
mutex mtx;

public : 


	customStream(streambuf* defsb){
		defaultBuf = defsb;
    };

	customStream(const char * data, unsigned int len);

	void registerThread(streambuf* threadsb){
		mtx.lock();
		files[gettid()] = threadsb;
		mtx.unlock();
	};

	void unregisterThread(){
		mtx.lock();
		files.erase(gettid());
		mtx.unlock();
	};

private:
	streamsize xsputn(const char* s, streamsize n){

		if (files.find(gettid()) != files.end()){
			files[gettid()]->sputn(s,n);
		}
		else {
			defaultBuf->sputn(s,n);
		}

    	return n;
	};


	int overflow (int c){
		
//		mtx.lock();
		int ret;

		if (files.find(gettid()) != files.end()){
            ret = files[gettid()]->sputc(c);
        }
        else {
            ret = defaultBuf->sputc(c);
        }


//		ret = defaultBuf->sputc(c);

//		mtx.unlock();

		return ret;
	};



	// This function is called when a flush is called by endl, adds an endline to the output
	int sync(){
//string output = filemap[gettid()];
//		string output = filemap[gettid()].c_str();
//		cout << endl;
//		char s[] = {'\n'};
//		int n = 1;
//		defaultBuf->sputn(s,n);
//		int r = defaultBuf->sputc('\n');

//		mtx.lock();
		int ret;

        if (files.find(gettid()) != files.end()){
            ret = files[gettid()]->pubsync();
        }
        else {
            ret = defaultBuf->pubsync();
        }

//		mtx.unlock();

//		ret = defaultBuf->pubsync();

//		printf("ret is %d\n", ret);
//		defaultBuf->rdbuf()->sputn(s,n);
		return 0;
	};


};
