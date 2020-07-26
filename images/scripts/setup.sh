git clone https://github.com/openenclave/openenclave.git

# Install Ansible
openenclave/scripts/ansible/install-ansible.sh && break

# Run Ansible Playbook
n=0
until [ "$n" -ge 5 ]
do
   ansible-playbook openenclave/scripts/ansible/oe-contributors-acc-setup-no-driver.yml && break
   n=$((n+1)) 
   sleep 15
done

# Remove openenclave
rm -rf openenclave