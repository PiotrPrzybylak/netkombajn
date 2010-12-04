mysqldump powercomputer -u root -p --no-data=true > database_structure.sql
mysqldump powercomputer -u root -p > database_structure_with_data.sql
mysqldump powercomputer -u root -p -t > database_data.sql