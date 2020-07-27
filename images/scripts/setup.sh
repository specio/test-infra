# Install Ansible
images/scripts/ansible/install-ansible.sh

# Run Ansible Playbook
n=0
until [ "$n" -ge 5 ]
do
   ansible-playbook images/scripts/ansible/oe-linux-ci-setup.yml && break
   n=$((n+1)) 
   sleep 15
done
