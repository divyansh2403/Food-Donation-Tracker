#include <stdio.h>

int main() {
    FILE *fptr;
    char filename[100], data[1000];

    // Get the file name and data from the user
    printf("Enter the filename to append data: ");
    scanf("%s", filename);

    // Open the file in append mode
    fptr = fopen(filename, "a");

    if (fptr == NULL) {
        printf("Error opening file!\n");
        return 1;
    }

    // Get data to append
    printf("Enter the data to append (press Enter to finish):\n");
    getchar();  // To consume newline character left by scanf
    fgets(data, sizeof(data), stdin);

    // Append data to the file
    fprintf(fptr, "%s", data);
    printf("Data appended successfully.\n");

    fclose(fptr);  // Close the file
    return 0;
}