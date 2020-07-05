  
# Use server core instead of nano because installing powershell is hard..
FROM mcr.microsoft.com/windows/servercore:ltsc2019

WORKDIR /
COPY "." .

#CAB File for the Windows Server 2019 Installation of the PSW on Windows Update
ENV PSW_CAB 48e7c1e9-6de8-46ee-8ff9-46aa7b7ee5b9_ab5277f79b04b450e6e7c3a624d99c1b1722e6f2.cab
 
#CAB File for the Windows Server 2019 Installation of DCAP on Windows Update
ENV DCAP_CAB 4f1f1691-c4b8-422c-9ca9-d22ebee726cc_a95a9c0e06caed691d2cbecae41bb0180b5a5fb5.cab
 
# Download the latest PSW And DCAP CAB files and then expand the required files into the appropriate directories
# Do this in one command to minimize container layers (reduces the size of the container)
 
RUN curl.exe -O http://download.windowsupdate.com/c/msdownload/update/driver/drvs/2020/05/%PSW_CAB% \
&& expand.exe /R %PSW_CAB% /F:sgx_urts.dll c:\windows\system32 \
&& expand /R %PSW_CAB% /F:*.dll c:\Windows\System32 \
&& expand /R %PSW_CAB% /F:third_party.rtf c:/ \
&& del /q %PSW_CAB% \
&& curl.exe -O http://download.windowsupdate.com/c/msdownload/update/driver/drvs/2020/05/%DCAP_CAB% \
&& expand /R %DCAP_CAB% /F:*.dll c:\Windows\System32 \
&& expand /R %DCAP_CAB% /F:License.txt c:/ \
&& expand /R %DCAP_CAB% /F:ThirdPartyLicenses.txt c:/ \
&& del /q %DCAP_CAB%

RUN cd images/windows/2019/ && install.cmd