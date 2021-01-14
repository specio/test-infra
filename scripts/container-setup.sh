# Install Ansible
scripts/ansible/install-ansible.sh

# Run Ansible Playbook
n=0
until [ "$n" -ge 5 ]
do
   ansible-playbook scripts/ansible/oe-contributors-acc-setup-no-driver.yml && break
   n=$((n+1)) 
   sleep 15
done
