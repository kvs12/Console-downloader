# Console-downloader
Console downloader which can run in several threads.
You can get jar archive by executing 'jar' gradle task.
Provide file with link for downloading and output filename divided by space.
Example of such file content:
http://ftp.at.debian.org/debian-cd/current-live/amd64/iso-hybrid/debian-live-8.2.0-amd64-cinnamon-desktop.iso first_debian_image.iso
http://ftp.at.debian.org/debian-cd/current-live/amd64/iso-hybrid/debian-live-8.2.0-amd64-lxde-desktop.iso second_debian_image.iso

usage: console-downloader
 -f <arg>   path to file with filenames and links which will be
            downloaded;
            example: http://site.org/file.zip archive.zip
 -l <arg>   general download speed limit, k and m suffixes are allowed; no
            speed limit by default
 -n <arg>   amount of concurrent downloading threads, default value = 2
 -o <arg>   existing output directory

