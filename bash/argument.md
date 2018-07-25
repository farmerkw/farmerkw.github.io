```bash
#!/bin/bash

usage() {
	echo "옵션 관련 출력"
	exit 1
}


FLAG_A=false
FLAG_B=false
while getopts "abc:d:" o; do
    case "${o}" in
        a)
            FLAG_A=true
            ;;
        b)
            FLAG_B=true
            ;;
        c)
            VALUE_C=${OPTARG}
            ;;
        d)
            ENUM_D=${OPTARG}
            [ "$ENUM_D" = "enum1" ] || [ "$ENUM_D" = "enum2" ] || usage
            ;;
        *)
            usage
            ;;
    esac
done

if [ -x $VALUE_C ]; then
	echo "VALUE C Not Define"
    usage
fi

echo "FLAG_A: ${FLAG_A}"
echo "FLAG_B: ${FLAG_B}"
echo "VALUE_C: ${VALUE_C}"
echo "ENUM_D: ${ENUM_D}"
```
