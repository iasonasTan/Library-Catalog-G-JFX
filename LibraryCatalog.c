#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[])
{
    if(argc <= 1) {
	printf("Error: no argument");
	exit(1);
    }
    char command[100] = "./bin/LibraryCatalogGJFX ";
    char* args = argv[1];
    strcat(command, args);
    system(command);
}
