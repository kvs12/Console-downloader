# Console-downloader
###Console downloader which can run in several threads.<br />
You can get jar archive by executing 'jar' gradle task. <br />
Provide file with link for downloading and output filename divided by space.  <br />
#####Example of such file content:<br />
---
http://ftp.at.debian.org/debian-cd/current-live/amd64/iso-hybrid/debian-live-8.2.0-amd64-cinnamon-desktop.iso first_debian_image.iso<br />
http://ftp.at.debian.org/debian-cd/current-live/amd64/iso-hybrid/debian-live-8.2.0-amd64-lxde-desktop.iso second_debian_image.iso<br />

usage: console-downloader<br />
 -f <arg>   path to file with filenames and links which will be
            downloaded;<br />
            example: http://site.org/file.zip archive.zip<br />
 -l <arg>   general download speed limit, k and m suffixes are allowed; no
            speed limit by default<br />
 -n <arg>   amount of concurrent downloading threads, default value = 2<br />
 -o <arg>   existing output directory<br />

