# Contents
 - [Docker in Windows](#Docker_in_Windows) 
 - [Git submodules](#Git_submodules)
 - [Miscellaneous links](#Miscellaneous_links)


##    Docker_in_Windows 




Reference  :
- https://www.sitepoint.com/docker-windows-10-home/
- https://blog.jayway.com/2017/04/19/running-docker-on-bash-on-windows/

- Steps to do (done so far):
 - (In Windows Powershell as admin)
    -   choco install virtualbox
    -   choco install docker-machine
 - (In Git bash)
    -   docker-machine create --driver virtualbox default (causing this error: VT-x is disabled in the BIOS for all CPU modes (VERR_VMX_MSR_ALL_VMX_DISABLED).)

################################################################################# 
## Git_submodules 

1. To add a git repo as submodule in another repo (in that specific folder):
git submodule add https://github.com/aqwertyuiop48/vuejs_training.git
git submodule add https://github.com/aqwertyuiop48/vercel_flask_app.git
git submodule add https://github.com/aqwertyuiop48/vercel_django_example.git
git submodule add https://github.com/aqwertyuiop48/express_vercel_app.git
git submodule add https://github.com/aqwertyuiop48/vue_project.git
git submodule add https://github.com/aqwertyuiop48/JavaScript-Applications.git

###### reference: https://stackoverflow.com/questions/1030169/pull-latest-changes-for-all-git-submodules
2. To update all the submodules according to the latest remote repo changes:
git submodule update --init --recursive
git submodule update --recursive


3. To fetch the all the submodules according to the latest remote repo changes:
git pull --recurse-submodules


4. Most important commands: 
git submodule add <git_repo_link>   (in that specific folder - when adding the module for the first time)
git submodule update --init --force --remote   (in the main folder)
git submodule add -b <branch A> --name <name A> --url <path A> 
 -eg:  git submodule add -b main https://github.com/aqwertyuiop48/my_angular_app.git javascript/angular_/my_angular_app_main

To remove a submodule:
 - Delete the relevant section from the .gitmodules file.
 - git add .gitmodules
 - git rm --cached <path-to-submodule>  (no trailing slash).
 - rm -rf .git/modules/<path_to_submodule>
 - git commit -m "Removed submodule <name>"
 - rm -rf <path_to_submodule>

5. Steps:
(i) git pull origin main
(ii) git submodule update --init --force --remote
(iii) git add .
(iv) git commit -m "Message"
(v) git push origin main

6. 
- Delete branch ("typescript") locally: git branch -D typescript
- Delete branch ("typescript") in remote : git push https://github.com/aqwertyuiop48/node_server.git --delete typescript




(All in one step) : 

<pre>
## For Github codespace
- unset GITHUB_TOKEN
- gh auth login
      Select: GitHub.com
      Select: HTTPS
      Select: Yes (Authenticate Git with your GitHub credentials)
      Select: Login with a web browser
</pre>

Mac:
git add . && git commit -m "Message" && git push origin main

<hr>
- For local system: <br>

alias mac='cd ~/Desktop/sreedhar/git4_/programming_languages && chmod +x add_modules.sh && ./add_modules.sh && git pull origin main && git submodule update --init --force --recursive --remote && git add . && git commit -m "Message" && git push origin main'

<hr>
- For Github codespace: <br>

alias mac_public='cd ~/Desktop/sreedhar/git4_/programming_languages && chmod +x add_modules_public.sh && ./add_modules_public.sh && git pull origin main && git -c submodule.certifications.update=none -c submodule.credentials_/credentials.update=none -c submodule.gate.update=none -c submodule.profiles.update=none submodule update --init --force --recursive && git add . && git commit -m "Message" && git push origin main'

<hr>

Windows:
git add . ; git commit -m "Message" ; git push origin main
git pull origin main ; git submodule update --init --force --remote ; git add . ; git commit -m "Message" ; git push origin main


Windows long file name issue:
1. Execute this command in Powershell (run as Administrator):  git config --system core.longpaths true

================================================================
## Miscellaneous_links

1. Youtube Channel content (in "Projects" playlist):
https://studio.youtube.com/channel/UC-RGFZerA05PxdPGCBV9RTw/videos/upload?filter=%5B%5D&sort=%7B%22columnType%22%3A%22date%22%2C%22sortOrder%22%3A%22DESCENDING%22%7D

2. Video compressor: (10MB video size limit to upload videos directly onto Github)
https://www.freeconvert.com/video-compressor

3. Video splitter:
https://split-video.com/


================================================================


## Cengage material

- https://github.com/aqwertyuiop48/profiles/releases
