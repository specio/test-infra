######
This document is to highlight some possible scenarios that you can run into whole using prow. 

Please read this document in this way.

Error description
```
Error example output.
```
Explaination: Why this happens, if it is a bug, expected and what can be done in the future.

Resolution: Do this to unblock yourself.


###
Could not resolve host: github

```
# FAILED!
# Cloning openenclave/openenclave at master
$ mkdir -p /home/prow/go/src/github.com/openenclave/openenclave
$ git init
Initialized empty Git repository in /home/prow/go/src/github.com/openenclave/openenclave/.git/
$ git config user.name ci-robot
$ git config user.email ci-robot@k8s.io
$ git fetch https://github.com/openenclave/openenclave.git --tags --prune
fatal: unable to access 'https://github.com/openenclave/openenclave.git/': Could not resolve host: github.com
# Error: exit status 128
```
Explaination: What we have witnessed is this happens when a great many 30+ tests are triggered at the same time. This is likely due to the underlying github api, causing some to pass and some to not be able to resolve the DNS. The long term solution is to allow multiple clusters to build jobs and distribute the workload.

Resolution: comment /retest on your PR, it's considered a flake at this time.

Jenkins jobs: No JSON object

```
Traceback (most recent call last):
  File "/hack/jenkins_bootstrap.py", line 177, in <module>
    myrigger.main()
  File "/hack/jenkins_bootstrap.py", line 170, in main
    job_number = self.waiting_for_job_to_start(queue_url)
  File "/hack/jenkins_bootstrap.py", line 96, in waiting_for_job_to_start
    if queue_request.json().get("why") != None:
  File "/usr/local/lib/python2.7/dist-packages/requests/models.py", line 898, in json
    return complexjson.loads(self.text, **kwargs)
  File "/usr/lib/python2.7/json/__init__.py", line 339, in loads
    return _default_decoder.decode(s)
  File "/usr/lib/python2.7/json/decoder.py", line 364, in decode
    obj, end = self.raw_decode(s, idx=_w(s, 0).end())
  File "/usr/lib/python2.7/json/decoder.py", line 382, in raw_decode
    raise ValueError("No JSON object could be decoded")
ValueError: No JSON object could be decoded
```
Explaination: A job is being called without the correct parameters, this points at there being a mismatch between the tests being run and the Jenkins job configuration.

Resolution: File a bug at openenclave/test-infra with the build log and job name. This should not be possible.