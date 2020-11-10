# Onboarding Repository Jobs 

## Overview

Onboarding jobs to prow is simplple. There are 3 types of jobs pre-submit, post-submit and periodic. Simple examples can be seen [here](../../config/jobs/oeedger8r-cpp). There are also 2 agents types, an agent type is a build environment. If you need anything complicated, SGX support, Windows, RHEL etc it is reccomended to use the Jenkins agents.

## Adding or Updating Jobs

## Adding or Updating Jobs

1. Find or create the prowjob config file in this directory
    - In general jobs for `github.com/openencalve/repo` use `repo/filename.yaml`
    - Ensure `filename.yaml` is unique across the config subdir; prow uses this as a key in its configmap
1. Ensure an [`OWNERS`](https://github.com/openenclave/test-infra/blob/master/OWNERS) file exists in the directory for job, and has appropriate approvers/reviewers
1. Write or edit the job config (please see [how-to-add-new-jobs](/prow/jobs.md#how-to-configure-new-jobs))
1. Ensure the job is configured to to display its results in [testgrid.k8s.io]
    - The simple way: add [testgrid annotations]
    - Please see the testgrid [documentation](/testgrid/config.md) for more details on configuation options
1. Open a PR with the changes; when it merges [@k8s-ci-robot] will deploy the changes automatically


# Jenkins jobs

## Pre-submit Jobs

Presubmit config looks like so:

```yaml
  - name: pr-oeedger8r-cpp-jenkins-ping
    branches:
    - master
    always_run: true
    decorate: true
    skip_report: false
    max_concurrency: 10
    spec:
      containers:
        - image: openenclave/jenkinsoperator:latest
          command:
            - sh
            - "-c"
            - "python /hack/jenkins_bootstrap.py --job test-infra/job/jenkins-ping --jenkins-user $JENKINS_USER --url $JENKINS_URL --jenkins-password $JENKINS_TOKEN"
```

## Periodic Jobs

Periodic config looks like so:

```yaml
  - name: ci-oeedger8r-cpp-lin-1804-debug
    extra_refs:
    - org: openenclave
      repo: oeedger8r-cpp
      base_ref: master
    decorate: true
    interval: 6h17m
    spec:
      containers:
        - image: openenclave/jenkinsoperator:latest
          command:
            - sh
            - "-c"
            - "python /hack/jenkins_bootstrap.py --job oeedger8r-cpp/job/Ubuntu1804Build --jenkins-user $JENKINS_USER --url $JENKINS_URL --jenkins-password $JENKINS_TOKEN --parameters OE_TEST_INFRA_PULL_NUMBER=master,OE_PULL_NUMBER=master,TEST_INFRA=false,LINUX_VERSION=1804,BUILD_TYPE=Debug"
```

## Post-submit Jobs

Postsubmit config looks like so:

```yaml
  - name: post-oeedger8r-cpp-rhel-8-debug
    branches:
    - master
    decorate: true
    spec:
      containers:
        - image: openenclave/jenkinsoperator:latest
          command:
            - sh
            - "-c"
            - "python /hack/jenkins_bootstrap.py --job oeedger8r-cpp/job/Rhel8Build --jenkins-user $JENKINS_USER --url $JENKINS_URL --jenkins-password $JENKINS_TOKEN --parameters OE_TEST_INFRA_PULL_NUMBER=master,OE_PULL_NUMBER=master,TEST_INFRA=false,BUILD_TYPE=Debug"
```

# Prow jobs

## Periodic Jobs

Periodic config looks like so:

```yaml
periodics:
- name: foo-job         # Names need not be unique, but must match the regex ^[A-Za-z0-9-._]+$
  decorate: true        # Enable Pod Utility decoration. (see below)
  interval: 1h          # Anything that can be parsed by time.ParseDuration.
  # Alternatively use a cron instead of an interval, for example:
  # cron: "05 15 * * 1-5"  # Run at 7:05 PST (15:05 UTC) every M-F
  spec: {}              # Valid Kubernetes PodSpec.
```


## Post-submit Jobs

Postsubmit config looks like so:

```yaml
postsubmits:
  org/repo:
  - name: bar-job         # As for periodics.
    decorate: true        # As for periodics.
    spec: {}              # As for periodics.
    max_concurrency: 10   # Run no more than this number concurrently.
    branches:             # Regexps, only run against these branches.
    - ^master$
    skip_branches:        # Regexps, do not run against these branches.
    - ^release-.*$
```

Postsubmits are run when a push event happens on a repo, hence they are
configured per-repo. If no `branches` are specified, then they will run against
every branch.

## Pre-submit Jobs

Presubmit config looks like so:

```yaml
presubmits:
  org/repo:
  - name: qux-job            # As for periodics.
    decorate: true           # As for periodics.
    always_run: true         # Run for every PR, or only when requested.
    run_if_changed: "qux/.*" # Regexp, only run on certain changed files.
    skip_report: true        # Whether to skip setting a status on GitHub.
    context: qux-job         # Status context. Defaults to the job name.
    max_concurrency: 10      # As for postsubmits.
    spec: {}                 # As for periodics.
    branches: []             # As for postsubmits.
    skip_branches: []        # As for postsubmits.
    trigger: "(?m)qux test this( please)?" # Regexp, see discussion.
    rerun_command: "qux test this please"  # String, see discussion.
```

If you only want to run tests when specific files are touched, you can use
`run_if_changed`. A useful pattern when adding new jobs is to start with
`always_run` set to false and `skip_report` set to true. Test it out a few
times by manually triggering, then switch `always_run` to true. Watch for a
couple days, then switch `skip_report` to false.

The `trigger` is a regexp that matches the `rerun_command`. Users will be told
to input the `rerun_command` when they want to rerun the job. Actually, anything
that matches `trigger` will suffice. This is useful if you want to make one
command that reruns all jobs. If unspecified, the default configuration makes
`/test <job-name>` trigger the job.


## Deleting Jobs

1. Find the prowjob config file in this directory
1. Remove the entry for your job; if that was the last job in the file, remove the file
1. If the job had no [testgrid annotations], ensure its [`testgrid/config.yaml`] entries are gone
1. Open a PR with the changes; when it merges [@k8s-ci-robot] will deploy the changes automatically


## Standard Triggering and Execution Behavior for Jobs

When configuring jobs, it is necessary to keep in mind the set of rules Prow has
for triggering jobs, the GitHub status contexts that those jobs provide, and the
rules for protecting those contexts on branches.

## Triggering Jobs
### Trigger Types

`prow` will consider three different types of jobs that run on pull requests
(presubmits):

 1. jobs that run unconditionally and automatically. All jobs that set
     `always_run: true` fall into this set.
 2. jobs that run conditionally, but automatically. All jobs that set
    `run_if_changed` to some value fall into this set.
 3. jobs that run conditionally, but not automatically. All jobs that set
    `always_run: false` and do not set `run_if_changed` to any value fall
    into this set and require a human to trigger them with a command.

By default, jobs fall into the third category and must have their `always_run` or
`run_if_changed` configured to operate differently.

In the rest of this document, "a job running unconditionally" indicates that the
job will run even if it is normally conditional and the conditions are not met.
Similarly, "a job running conditionally" indicates that the job runs if all of its
conditions are met.

## Triggering Jobs With Comments

A developer may trigger presubmits by posting a comment to a pull request that
contains one or more of the following phrases:
 - `/test job-name` : When posting `/test job-name`, any jobs with matching triggers
   will be triggered unconditionally.
 - `/retest` : When posting `/retest`, two types of jobs will be triggered:
   - all jobs that have run and failed will run unconditionally
   - any not-yet-executed automatically run jobs will run conditionally
 - `/test all` : When posting `/test all`, all automatically run jobs will run
   conditionally.

Note: It is possible to configure a job's `trigger` to match any of the above keywords
(`/retest` and/or `/test all`) but this behavior is not suggested as it will confuse
developers that expect consistent behavior from these commands. More generally, it is
possible to configure a job's `trigger` to match any command that is otherwise known
to Prow in some other context, like `/close`. It is similarly not suggested to do this.

## Posting GitHub Status Contexts

Presubmit and postsubmit jobs post a status context to the GitHub
commit under test once they start, unless the job is configured
with `skip_report: true`.

Use a `/retest` or `/test job-name` to re-trigger the test and
hopefully update the failed context to passing.

If a job should no longer trigger on the pull request, use the
`/skip` command to dismiss a failing status context (depends on
`skip` plugin).

Repo administrators can also `/override job-name` in case of emergency
(depends on the `override` plugin).
