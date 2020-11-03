# Creating Secure Public GitHub Continuous Integration Pipelines

## Specific Rules

### Secret data must not be stored in source code

Source code should never contain secrets per the “Services".

Even test and alerting credentials must not be stored within the source code.

### Secret data must not be stored in build or release pipelines

Although it may seem secure to store hardcoded secrets on the pipelines used for PR CIs, it’s very easy to add additional components to a pull request that output various information about the running steps as said steps occur. In YAML pipelines the chances of this being an issue is lower because the build steps are publicly readable by anyone syncing the code and a secret scanner will be used for alerting..

### Secret data must not be stored in variables

Once the attacker can output data for arbitrary stages in a build pipeline, they can easily dump the contents of variables in the build by dumping the contents of the current environment variables. While by default this will not reveal variables labeled “secret”, these variables can still be accessed using the inline commands

### Secret data must not be stored in variable groupings

In the case of YAML pipelines it’s possible for an attacker to selectively include variable groupings4 that were never intended to be used by a specific pipeline.

Although it can be tempting to store secret data in variable groupings that have “Allow access to all pipelines” disabled, this relies on isolation at a more granular security boundary then the project level. Since build agents contain a token that’s allowed to modify all other project resources at a minimum5, it’s possible for a compromised build agent to pivot to the pipeline that’s granted access to the variable grouping.

### Secret data must not be stored on build agents

Since malicious pull requests effectively result in arbitrary code execution on the build agents, it’s always possible for a malicious build to leak any information that the build agent process has access to. In addition, secret data held on a build agent may leak in diagnostic data sources that may be available to the public, such as the Build Agent Capabilities6 tab.

### Build agents must not use identities that are valid in any other context

Since build agents running public CI pipelines must always be considered under attacker control, the agent identity is also under attacker control. If agents are run with corporate credentials, the build agent can use the identity to begin attacking remotely accessible resources.

### Build agents must be isolated from any other non-public network

Once the build agent is under attacker control the attacker can begin to conduct more in-depth attacks, potentially while other builds are running. If a build agent is joined to the attacker can begin performing insider threat attacks from their trusted position within the network.

### Build machines must be monitored for malicious behavior

Even if the attacker does not obtain privileged credentials or network access, compute and network time itself is valuable to attackers. It’s possible for an attacker to use a build machine that has been compromised to perform malicious behavior such as spamming, denial of service, or cryptocurrency mining. At the extreme end of the spectrum the attacker could use the machine as a proxy for other.

### Build pools must not be shared between public PR CI pipelines and trusted builds

Given that build agents can always compromise other resources in the same project, it’s not possible to trust a build pool that ever builds malicious code. If the attacker persists on a build agent, they can compromise any future build that agent produces. They could also use the agent’s token to modify artifacts that were produced for a trusted build. At the limits they could use a build running concurrent to a trusted build to affect other build agents in the same pool.

### The trusted internal build must build entirely cleanly from source for its production purposes.

Public users must not be able to define or modify build pipelines, build pools, or service connections

Although attackers will always be able to compromise builds that they can submit pull requests for, allowing them to modify an existing pipeline will substantially increase the risk of a successful denial of service against a public CI PR pipeline. Defining new pipelines will allow more resource consumption then existing pipelines that are attached to a build pool or selection of additional build pools. Defining new build pools can be used in various luring attacks, and service connections will allow the attacker to delegate their own permissions to construct more automated attacks.

### All code must be reviewed for security attacks before being accepted into a trusted repository

All these mitigations assume the fact that at some point a trusted production build will be produced. If code is accepted arbitrarily into the mainline repository (for instance, if a build is clean and a test suite passes) it’s possible for the attacker to trivially compromise the production instance by creating an exploit that’s sufficiently non-obvious to casual readers.

### General Guidance

Service connections should be limited to minimum possible privileges to support the PR CI scenario

At a minimum a public CI pipeline taking pull requests will need to authenticate to the source of pull requests in order to update the status of the CI pipeline. This token should be scoped to only the activity it absolutely needs to perform to support the business scenario. It should never be a full-scoped service connection.

Additional service connections should only be added after a strong business case and an evaluation of the impact of a compromised service connection.

Since a compromised build agent can use a compromised service connection, it should always be assumed that service connections for public CI pipelines are under attacker control.

### Build pools should use hosted build agents whenever possible

Hosted build agents are run and maintained by CI teams and are not maintained by any repo team directly. The responsibility of upgrades, maintenance, and scaling is owned by a centralized team with a vested interest in ensuring low-friction secure build processes. These machines are also discarded after every build, minimizing the time that compromised builds can affect other resources.

### Non hosted build agents should block any unnecessary Internet access

Since build agents represent free resources for attackers these resources should be scoped to the minimum possible risk to other networks, including the broader Internet as a whole. Although some access will be necessary to communicate with integration services and GitHub, blocking of other traffic should be implemented whenever possible. Blocking port 25 and websites that are unnecessary can discourage spamming behavior, blocking outbound management ports can discourage brute forcing, and blocking known cryptocurrency communication channels can discourage mining.

This does represent a considerable amount of operational overhead and is not the highest impact item.

### Build machines should be frequently rebuilt to prevent persistence on build machines

In order to prevent long-term persistence and use of build machines for malicious purposes build machines should frequently be torn down and reset to known-good state.

### Security Concerns / Reporting

All security concerns should be brought up with the repo admins following their elected process and not posted publicly.

### Obvious bits

These are less interesting, but anything accessible needs to be compliant to the terms of the hosting teams, think HTTPS/SSL/Secret rotation etc.

### Example of a secure GitHub CI pipeline for public pull requests

This section discusses an example project which is publicly hosted on GitHub, accepts public Pull Requests into a Continuous Integration pipeline. It separates the code and integration pieces into a decoupled form to allow anything else necessary for compliance to happen behind the scenes.

This product uses two distinct integration environments for isolation purposes. The first of these groups, “Public”, is the organization used for the GitHub CI Pipeline. The second organization “Internal”, is for the official release builds of the product, as well as for sensitive (EG: security) or internal changes performed outside of public view. These are where Azure/Intel monitoring software can be installed in agents etc.

The public environment has one or more build pools dedicated to performing GitHub CI pipelines and no build pools for other purposes. When a public PR is filed on a hosted build agent is assigned to perform the build from the GitHub repository.

None of these public agents use or have access to secret data as a malicious Pull Request could compromise any data they utilize. Likewise, they use untrusted identities and networks that are isolated from trusted resources and have only minimum of Internet access. Fundamentally completing a malicious PR cannot gain an attacker any more substantial of a foothold then spoofing a passed CI pipeline that should have failed.

At the same time the internal organization is using a separate build pool to perform its internal functions allowing everything to be conducted in an open and accessible way, while adhering to any extra security processes.